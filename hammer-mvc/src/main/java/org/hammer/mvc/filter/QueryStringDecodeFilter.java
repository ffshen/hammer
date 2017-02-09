package org.hammer.mvc.filter;


import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.hammer.utils.Encodes;

import java.io.IOException; 

/**
 * 
 * @author shenx
 * @date 2017年2月6日
 * @see
 * @version
 */
public class QueryStringDecodeFilter implements Filter {
    
    public static final String FilterName = "QueryStringDecodeFilter" ;
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try {
            ServletRequest requestWrapper = new HttpServletRequestWrapper((HttpServletRequest) servletRequest) {
                @Override
                public String getQueryString() {
                    try{
                        return Encodes.urlDecode(((HttpServletRequest) servletRequest).getQueryString());
                    }
                    catch(Exception ex){
                        return ((HttpServletRequest) servletRequest).getQueryString() ;
                    }
                }
            };
            filterChain.doFilter(requestWrapper, servletResponse);
        } catch (Exception ex) {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    @Override
    public void destroy() {

    }
}
