package org.hammer.zookeeper.listener.impl;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.log4j.Logger;
import org.apache.zookeeper.CreateMode;
import org.hammer.zookeeper.listener.IConnectionStateListener;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service("ReconnectHandler")
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class ReconnectHandler implements IConnectionStateListener {
	
	private static final Logger logger =  Logger.getLogger(ReconnectHandler.class);
	
	private static final String path = "/errorhandle/handle-provider-";
	
	private static final String payload = "";
	
	public ReconnectHandler(){
	}
	
	@Override
	public void stateChanged(CuratorFramework client, ConnectionState newState) {
		/**
		当连接出现异常, 将通过ConnectionStateListener接口进行监听, 并进行相应的处理, 这些状态变化包括: 
			暂停(SUSPENDED): 当连接丢失, 将暂停所有操作, 直到连接重新建立, 如果在规定时间内无法建立连接, 将触发LOST通知
			重连(RECONNECTED): 连接丢失, 执行重连时, 将触发该通知
			丢失(LOST): 连接超时时, 将触发该通知
			*/
		if (newState == ConnectionState.LOST) {
			  logger.fatal("ZooKeeper Lost Connection!");
		      while (true) {
		        try {
		          if (client.getZookeeperClient().blockUntilConnectedOrTimedOut()) {
		        	  client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath(path, payload.getBytes("UTF-8"));
		        	  break;
		          }
		        } catch (InterruptedException e) {
		        	logger.error(e);
		        } catch (Exception e) {
		        	logger.error(e);
		        }
		      }
		}
		
	}


}
