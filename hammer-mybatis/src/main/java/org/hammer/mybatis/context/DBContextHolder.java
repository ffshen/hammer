package org.hammer.mybatis.context;

/**
 * 
 * @author shenx
 * @date 2017年2月6日
 * @see
 * @version
 */
public class DBContextHolder {
    private static final ThreadLocal<String> contextHolder = new ThreadLocal<String>();
    private final static String DB_TYPE_RW = "dataSourceKeyRW";
    private final static String DB_TYPE_R = "dataSourceKeyR";

    public enum DBType {
        RW(DB_TYPE_RW), 
        
        R(DB_TYPE_R)
        ;
        private String type;
        
        private DBType(String type) {
            this.type = type;
        }

        public String value() {
            return type;
        }
    }

    public DBContextHolder() {
    }

    public static void setDBType(String dbType) { 
        contextHolder.set(dbType);
    }

    public static String getDBType() {
        String db = contextHolder.get();
        if (db == null) {
            db = DB_TYPE_RW;// 默认是读写库
        }
        return db;
    }

    public static void clearDBType() {
        contextHolder.remove();
    }
}
