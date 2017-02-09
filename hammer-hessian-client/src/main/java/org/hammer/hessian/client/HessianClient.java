package org.hammer.hessian.client;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Properties;

import org.hammer.constants.HessianClientConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.caucho.hessian.client.HessianProxyFactory; 

/**
 * 
 * @author shenx
 * @date 2017年2月6日
 * @see
 * @version
 */
public abstract class HessianClient<T>  {
    
    public static String B2C_BASE = null;
    
    static
    {
        Properties props = new Properties();
        try
        {
            props.load(HessianClient.class.getResourceAsStream("/hessian/hessian.properties"));
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        B2C_BASE = props.getProperty("hessian.path",HessianClientConstants.SERVICE_PATH);
    }
    private final static Logger logger = LoggerFactory.getLogger(HessianClient.class);
    
    private final static HessianProxyFactory hessianProxyFactory = new CustomHessianProxyFactory();    
      
	@SuppressWarnings("unchecked")
	protected static <T> T createServiceApi(String url, Class<T> clazz) {
		T serviceApi = null;
        try {
            serviceApi = (T) hessianProxyFactory.create(clazz, url);
        } catch (MalformedURLException e) {
            logger.error("HessianClient createServiceApi error, url:{}, class:{}, ex:", url, clazz.getName(),  e);
        }
        return serviceApi;
    }
 
}
