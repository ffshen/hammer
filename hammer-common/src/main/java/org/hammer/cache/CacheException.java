package org.hammer.cache;

public class CacheException extends Exception {

    private static final long serialVersionUID = 1L;

    public CacheException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public CacheException(String msg) {
        super(msg);
    }

}
