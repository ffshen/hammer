package org.hammer;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;

import org.apache.curator.utils.PathUtils;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try{
			PathUtils.validatePath("/kil");
			System.out.println(1);
			CuratorFramework client	= 	CuratorFrameworkFactory.newClient("localhost:2181", new ExponentialBackoffRetry(1000, 3));
			System.out.println(2);
			client.start();
			System.out.println(3);
			InterProcessMutex lock = new InterProcessMutex(client, "/examples/locks");
			System.out.println(4);
			lock.acquire();
			System.out.println(5);
			lock.release();
			System.out.println(6);
		}
		catch(Exception ex){
			System.out.println(ex.toString());
		}
	}

}
