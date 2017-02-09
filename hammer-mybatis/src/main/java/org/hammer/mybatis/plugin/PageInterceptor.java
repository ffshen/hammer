package org.hammer.mybatis.plugin;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.executor.ExecutorException;
import org.apache.ibatis.executor.statement.BaseStatementHandler;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ParameterMode;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.property.PropertyTokenizer;
import org.apache.ibatis.scripting.xmltags.ForEachSqlNode;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.hammer.utils.ReflectHelper;
import org.hammer.vo.Pageable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 


@Intercepts({@Signature(type=StatementHandler.class,method="prepare",args={Connection.class,Integer.class})})
public class PageInterceptor implements Interceptor {
    private Logger logger = LoggerFactory.getLogger(getClass());
	private static String dialect = "";	//数据库方言
	private static String pageSqlId = ""; //mapper.xml中需要拦截的ID(正则匹配)
	
	public Object intercept(Invocation ivk) throws Throwable {
		if(ivk.getTarget() instanceof RoutingStatementHandler){
			RoutingStatementHandler statementHandler = (RoutingStatementHandler)ivk.getTarget();
			BaseStatementHandler delegate = (BaseStatementHandler) ReflectHelper.getValueByFieldName(statementHandler, "delegate");
			MappedStatement mappedStatement = (MappedStatement) ReflectHelper.getValueByFieldName(delegate, "mappedStatement");

			if(mappedStatement.getId().matches(pageSqlId)){ //拦截需要分页的SQL
				BoundSql boundSql = delegate.getBoundSql();
				Object parameterObject = boundSql.getParameterObject();//分页SQL<select>中parameterType属性对应的实体参数，即Mapper接口中执行分页方法的参数,该参数不得为空
				if(parameterObject==null){
					throw new NullPointerException("parameterObject尚未实例化！");
				}else{
					Connection connection = (Connection)ivk.getArgs()[0];
					String sql = boundSql.getSql();
					String countSql = genCountSql(sql); //记录统计
					PreparedStatement countStmt = connection.prepareStatement(countSql);
					BoundSql countBS = new BoundSql(mappedStatement.getConfiguration(),countSql,boundSql.getParameterMappings(),parameterObject);
					setParameters(countStmt,mappedStatement,countBS,parameterObject);
                    logger.info("Executing pageable count sql:" + countSql);
					ResultSet rs = countStmt.executeQuery();
					int count = 0;
					boolean isGroupBy = isGroupBy(sql);
					while(rs.next()) {
						if(isGroupBy) {
							count ++;
						} else {
							count = rs.getInt(1);
						}
					}
					rs.close();
					countStmt.close();
					logger.info("Executed pageable count sql, total count:" + count);
					Pageable page = null;
					if(parameterObject instanceof Pageable){	//参数就是Page实体
						page = (Pageable) parameterObject;
						page.setTotalCount(count);
					} else{	//参数为某个实体，该实体拥有Page属性
						Field pageField = ReflectHelper.getFieldByFieldName(parameterObject,"page");
						if(pageField!=null){
							page = (Pageable) ReflectHelper.getValueByFieldName(parameterObject,"page");
							if(page==null)
								page = new Pageable();
							page.setTotalCount(count);
							ReflectHelper.setValueByFieldName(parameterObject,"page", page); //通过反射，对实体对象设置分页对象
						}else{
							throw new NoSuchFieldException(parameterObject.getClass().getName()+"不存在 page 属性！");
						}
					}
					String pageSql = generatePageSql(sql,page);
					ReflectHelper.setValueByFieldName(boundSql, "sql", pageSql); //将分页sql语句反射回BoundSql.
				} 
			}
		}
		return ivk.proceed();
	}


    private String genCountSql(String sql) {
        String countSql = "SELECT count(1) FROM ";
        String[] sqlWords = sql.trim().replaceAll("\\t", " ").replaceAll("\\n", " ").split(" ");
        Stack<String> selectStack = new Stack<String>();
        boolean fromStarted = false;
        for(String sqlWord : sqlWords) {
            if(fromStarted) {
                countSql += sqlWord + " ";
                continue;
            }
            if(sqlWord.trim().toUpperCase().equals("SELECT") || sqlWord.toUpperCase().equals("(SELECT")){
                selectStack.push(sqlWord);
            }
            if(sqlWord.trim().toUpperCase().equals("FROM")) {
                selectStack.pop();
                if(selectStack.isEmpty()) {
                    fromStarted = true;
                }
            }
            
        }
        logger.info("Pagination Oringin SQL:" + sql);
        logger.info("Pagination Count SQL:" + countSql);
        return countSql;
    }

	
	/**
	 * 对SQL参数(?)设值,参考org.apache.ibatis.executor.parameter.DefaultParameterHandler
	 * @param ps
	 * @param mappedStatement
	 * @param boundSql
	 * @param parameterObject
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
    private void setParameters(PreparedStatement ps,MappedStatement mappedStatement,BoundSql boundSql,Object parameterObject) throws SQLException {
		ErrorContext.instance().activity("setting parameters").object(mappedStatement.getParameterMap().getId());
		List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
		if (parameterMappings != null) {
			Configuration configuration = mappedStatement.getConfiguration();
			TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
			MetaObject metaObject = parameterObject == null ? null: configuration.newMetaObject(parameterObject);
			for (int i = 0; i < parameterMappings.size(); i++) {
				ParameterMapping parameterMapping = parameterMappings.get(i);
				if (parameterMapping.getMode() != ParameterMode.OUT) {
					Object value;
					String propertyName = parameterMapping.getProperty();
					PropertyTokenizer prop = new PropertyTokenizer(propertyName);
					if (parameterObject == null) {
						value = null;
					} else if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
						value = parameterObject;
					} else if (boundSql.hasAdditionalParameter(propertyName)) {
						value = boundSql.getAdditionalParameter(propertyName);
					} else if (propertyName.startsWith(ForEachSqlNode.ITEM_PREFIX)&& boundSql.hasAdditionalParameter(prop.getName())) {
						value = boundSql.getAdditionalParameter(prop.getName());
						if (value != null) {
							value = configuration.newMetaObject(value).getValue(propertyName.substring(prop.getName().length()));
						}
					} else {
						value = metaObject == null ? null : metaObject.getValue(propertyName);
					}
					@SuppressWarnings("rawtypes")
                    TypeHandler typeHandler = parameterMapping.getTypeHandler();
					if (typeHandler == null) {
						throw new ExecutorException("There was no TypeHandler found for parameter "+ propertyName + " of statement "+ mappedStatement.getId());
					}
					typeHandler.setParameter(ps, i + 1, value, parameterMapping.getJdbcType());
				}
			}
		}
	}
	
	/**
	 * 根据数据库方言，生成特定的分页sql
	 * @param sql
	 * @param page
	 * @return
	 */
	private String generatePageSql(String sql,Pageable page){
		if(page!=null && dialect!=null && dialect.trim().length()>0){
			StringBuffer pageSql = new StringBuffer();
			if("mysql".equals(dialect)){
				pageSql.append(sql);
				pageSql.append(" limit "+page.getStart()+","+page.getPageSize());
			}else if("oracle".equals(dialect)){
				pageSql.append("select * from (select tmp_tb.*,ROWNUM row_id from (");
				pageSql.append(sql);
				pageSql.append(") as tmp_tb where ROWNUM<=");
				pageSql.append(page.getStart()+page.getPageSize());
				pageSql.append(") where row_id>");
				pageSql.append(page.getStart());
			}
			return pageSql.toString();
		}else{
			return sql;
		}
	}
	
	public Object plugin(Object arg0) {
		// TODO Auto-generated method stub
		return Plugin.wrap(arg0, this);
	}

	public void setProperties(Properties p) {
		dialect = p.getProperty("dialect");
//		if (dialect!=null && dialect.trim().length()>0) {
//			System.out.println("dialect property is not found!");
//		}
		pageSqlId = p.getProperty("pageSqlId");
//		if (pageSqlId!=null && pageSqlId.trim().length()>0) {
//				System.out.println("pageSqlId property is not found!");
//		}
	}

	public static boolean isGroupBy(String sql) {
		String regEx = "group\\s+by\\s+\\S+\\s*(order\\s+by\\s+\\S+(\\s+desc)?(\\s+asc)?\\s*)?$";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(sql);
		boolean isGroupBy = m.find();
		return isGroupBy;
	}
	
}
