package org.hammer.context.generator;

import java.util.concurrent.ThreadLocalRandom;

public class TraceIdGenerator {
    
    public final static String DEFAULT_TRACE_ID_PREFIX = "000000";
    
    public static String create() {
        return create(DEFAULT_TRACE_ID_PREFIX);
    }    
    
    public static String create(Object id) {
        if(id == null) {
            id = DEFAULT_TRACE_ID_PREFIX;
        }
        Long utc = System.currentTimeMillis();
        Integer random = ThreadLocalRandom.current().nextInt();
        String traceId = "" + id + utc + Math.abs(random);
        return traceId;
    }
}
