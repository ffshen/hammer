package org.hammer.lock;

import java.util.concurrent.TimeUnit;

/**
 * Created by neron.liu on 8/25/16.
 */
public class NamedLock implements LockObject {

    private String lockName;

    private ILock lock;

    public NamedLock(String lockName, ILock lock) {
        this.lockName = lockName;
        this.lock = lock;
    }

    @Override
    public boolean exists() {
        return lock.exists(lockName);
    }

    @Override
    public boolean lock() throws LockAcquisitionException {
        return lock.lock(lockName);
    }

    @Override
    public boolean lock(int seconds) throws LockAcquisitionException {
        return lock.lock(lockName, seconds);
    }

    @Override
    public boolean lock(int value, TimeUnit unit) throws LockAcquisitionException {
        return lock.lock(lockName, value, unit);
    }

    @Override
    public boolean unlock() throws LockReleaseException {
        return lock.unlock(lockName);
    }

}
