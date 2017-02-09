package org.hammer.lock;

/**
 * Created by neron.liu on 8/25/16.
 */
public class LockReleaseException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public LockReleaseException() {
    }

    public LockReleaseException(String message) {
        super(message);
    }

    public LockReleaseException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
