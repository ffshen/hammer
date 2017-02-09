package org.hammer.hessian.client;

import com.caucho.hessian.client.HessianProxyFactory;
import com.caucho.hessian.io.HessianRemoteObject;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.net.URL;

/**
 * Created by ansion on 16/7/9.
 */
public class CustomHessianProxyFactory extends HessianProxyFactory {

    @Override
    public Object create(Class<?> api, URL url, ClassLoader loader)
    {
        if (api == null) {
            throw new NullPointerException("api must not be null for HessianProxyFactory.create()");
        }

        InvocationHandler handler = new CustomHessianProxy(url, this, api);

        return Proxy.newProxyInstance(loader,
                new Class[]{api, HessianRemoteObject.class},
                handler);
    }

}
