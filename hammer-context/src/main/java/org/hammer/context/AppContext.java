package org.hammer.context;

import com.google.common.collect.Maps;
   

import java.util.Map;

import org.hammer.context.generator.TraceIdGenerator;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.crypto.hash.Md5Hash; 

public class AppContext {
    
    public final static String TRACE_ID = "traceId";
    
    public final static String API_SIGN = "apiSign";
    
    public final static String API_ID = "apiId";
    
    public final static String API_KEY = "apiKey";
    
    private  static String apiKey = "e807f1fcf82d132f9bb018ca6738a19f";
    
    private  static String apiSecret = "6fb42da0e32e07b61c9f0251fe627a9c";

    public final static Map<String, String> KEY_MAP = Maps.newHashMap();
    
    static { 
        KEY_MAP.put(TRACE_ID, TRACE_ID);
        KEY_MAP.put(TRACE_ID.toLowerCase(), TRACE_ID); 
        KEY_MAP.put(API_SIGN, API_SIGN);
        KEY_MAP.put(API_SIGN.toLowerCase(), API_SIGN);
        KEY_MAP.put(API_ID, API_ID);
        KEY_MAP.put(API_ID.toLowerCase(), API_ID);        
    }
    
    private static ThreadLocal<AppContext> current = new ThreadLocal<>();
    
    Map<String, Object> context = Maps.newHashMap();
    
    private boolean isNeedClear = true;

    public static boolean isNeedClear() {
        return current().isNeedClear;
    }

    public static boolean isEmpty() {
        return current.get() == null;
    }

    public static void setNeedClear(boolean needClear) {
        current().isNeedClear = needClear;
    }
    
    public static Object getApiSign() {
        return current().context.get(API_SIGN);
    }

    public static Object getApiId() {
        return current().context.get(API_ID);
    }
    
    public static String getTraceId() {
        Object traceId =  current().context.get(TRACE_ID);
        if(traceId == null) {
            traceId = TraceIdGenerator.create(null);
            current().context.put(TRACE_ID, traceId);
        }
        return traceId.toString();
    }

    public static boolean isTraceIdEmpty() {
        return current().context.get(TRACE_ID) == null;
    }

    public static void put(String name, Object value) {
        current().context.put(name, value);
    }
    public static Object get(String name) {
        return current().context.get(name);
    }
    public static void remove(String name) {
        current().context.remove(name);
    }

    public static AppContext current() {
        if(current.get() == null) {
            current.set(new AppContext());
        }
        return current.get();
    }

    public static void clear() {
        if(current.get() != null) {
            current.set(null);
        }
    }

    public static void current(AppContext hessianContext) {
        current.set(hessianContext);
    }

    public static AppContext copyFromCurrent() {
        AppContext newContext = new AppContext();
        newContext.isNeedClear = AppContext.isNeedClear();
        newContext.context.put(AppContext.TRACE_ID, AppContext.getTraceId());
        return newContext;
    }

	public static String getApiKey() {
		return apiKey;
	}

	public static void setApiKey(String apiKey) {
		AppContext.apiKey = apiKey;
	}

	public static String getApiSecret() {
		return apiSecret;
	}

	public static void setApiSecret(String apiSecret) {
		AppContext.apiSecret = apiSecret;
	}

    public static String createApiSign(String apiId,String apiSecret) {
        //mac based md5(userId + apiId + apiSecret)
        StringBuilder sb = new StringBuilder();
        sb.append(apiId) ;
        sb.append(apiSecret);
        String mac = (new Md5Hash(sb.toString())).toBase64();
        return mac;
    }
    
    public static Boolean apiSignIsMatch(String apiId,String apiKey,String apiSign){
    	String apiSecret = "" ;// by apiKey
    	return StringUtils.equals(apiSign, createApiSign(apiId,apiSecret));
    }
}
