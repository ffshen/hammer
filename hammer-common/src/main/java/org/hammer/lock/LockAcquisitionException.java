package org.hammer.lock;

/**
 * Created by neron.liu on 8/25/16.
 */
public class LockAcquisitionException extends Exception { 
	
	private static final long serialVersionUID = 1L;

	public LockAcquisitionException() {
    }

    public LockAcquisitionException(String message) {
        super(message);
    }

    public LockAcquisitionException(String message, Throwable cause) {
        super(message, cause);
    }

}
