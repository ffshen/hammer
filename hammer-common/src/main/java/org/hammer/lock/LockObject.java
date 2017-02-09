package org.hammer.lock;

import java.util.concurrent.TimeUnit;

/**
 * Created by neron.liu on 8/25/16.
 */
public interface LockObject {

    public boolean exists();

    /**
     * @return
     */
    public boolean lock() throws LockAcquisitionException;

    /**
     * @param seconds 锁的失效时间(秒)
     * @return
     */
    public boolean lock(int seconds) throws LockAcquisitionException;

    /**
     * @param value 锁的失效时间
     * @param unit  时间单位
     * @return
     */
    public boolean lock(int value, TimeUnit unit) throws LockAcquisitionException;

    /**
     * @return
     */
    public boolean unlock() throws LockReleaseException;

}
