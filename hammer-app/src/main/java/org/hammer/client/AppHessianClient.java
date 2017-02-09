package org.hammer.client;

import org.hammer.constants.HessianClientConstants;
import org.hammer.hessian.client.HessianClient;
import org.hammer.service.AppHessianClientAPI;

public class AppHessianClient extends HessianClient<AppHessianClientAPI> {

	private final static AppHessianClientAPI api =  createServiceApi(B2C_BASE + HessianClientConstants.SERVICE_API, AppHessianClientAPI.class);
	
	public static AppHessianClientAPI getHessianApi() {
		return api ;
	}

}
