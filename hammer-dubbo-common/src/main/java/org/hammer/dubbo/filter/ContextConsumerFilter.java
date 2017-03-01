package org.hammer.dubbo.filter;

import org.hammer.context.AppContext;

import com.alibaba.dubbo.rpc.Filter;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.dubbo.rpc.RpcException;

public class ContextConsumerFilter implements Filter{

	@Override
	public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
		RpcContext.getContext().setAttachment(AppContext.TRACE_ID, AppContext.getTraceId()) ;
		return invoker.invoke(invocation);
	}

}
