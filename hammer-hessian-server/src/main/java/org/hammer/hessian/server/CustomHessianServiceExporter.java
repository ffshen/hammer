package org.hammer.hessian.server;
 
import org.apache.commons.lang3.StringUtils;
import org.hammer.context.AppContext;
import org.springframework.remoting.caucho.HessianServiceExporter;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.util.NestedServletException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

public class CustomHessianServiceExporter extends HessianServiceExporter {


    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!"POST".equals(request.getMethod())) {
            throw new HttpRequestMethodNotSupportedException(request.getMethod(),
                    new String[] {"POST"}, "HessianServiceExporter only supports POST requests");
        }

        handleHessianHeader(request);

        response.setContentType(CONTENT_TYPE_HESSIAN);
        try {
            invoke(request.getInputStream(), response.getOutputStream());
        } catch (Throwable ex) {
            throw new NestedServletException("Hessian skeleton invocation failed", ex);
        }
    }
 
    @SuppressWarnings({ "rawtypes", "static-access" })
	protected void handleHessianHeader(HttpServletRequest request) {
        if(!AppContext.isEmpty() &&
                (StringUtils.equals(AppContext.getTraceId(), request.getHeader(AppContext.TRACE_ID))
         || StringUtils.equals(AppContext.getTraceId(), request.getHeader(AppContext.TRACE_ID.toLowerCase())))) {
            return;
        } 
        AppContext.clear();
        AppContext.setNeedClear(false);
        Enumeration enumeration = request.getHeaderNames();
        while (enumeration.hasMoreElements()) {
            String name = enumeration.nextElement().toString();
            String value = request.getHeader(name);
            String key = AppContext.KEY_MAP.get(name.toLowerCase());
            if(key != null) {
                AppContext.current().put(key, value);
            }
        }
    }

    public static HessianServiceExporter create(Object serviceApi, Class<?> apiClazz) {
        HessianServiceExporter exporter = new CustomHessianServiceExporter();
        exporter.setService(serviceApi);
        exporter.setServiceInterface(apiClazz);
        return exporter;
    }

}
