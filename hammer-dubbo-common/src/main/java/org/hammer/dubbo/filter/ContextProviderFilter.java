package org.hammer.dubbo.filter;
  

import org.hammer.context.AppContext;
import org.hammer.context.generator.TraceIdGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.rpc.Filter;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.dubbo.rpc.RpcException; 

public class ContextProviderFilter implements Filter {

    Logger logger = LoggerFactory.getLogger(this.getClass()); 
	@Override
	public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
		String traceId = RpcContext.getContext().getAttachment(AppContext.TRACE_ID) ;
		if(StringUtils.isEmpty(traceId) ){
			AppContext.put(AppContext.TRACE_ID, TraceIdGenerator.create());
		}
		else{
			AppContext.put(AppContext.TRACE_ID, traceId);			
		}
//		logger.info("ContextProviderFilter traceId ： {}" , AppContext.getTraceId() );
		return invoker.invoke(invocation);
	}

}
