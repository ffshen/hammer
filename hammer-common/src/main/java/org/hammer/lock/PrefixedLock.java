package org.hammer.lock;

import java.util.concurrent.TimeUnit;

/**
 * Created by neron.liu on 8/25/16.
 */
public class PrefixedLock implements ILock {

    private String prefix;

    private ILock lock;

    public PrefixedLock(String prefix, ILock lock) {
        this.prefix = prefix;
        this.lock = lock;
    }

    private String getPrefixedLockName(String lockName) {
        return prefix + lockName;
    }

    @Override
    public boolean exists(String lockName) {
        return lock.exists(getPrefixedLockName(lockName));
    }

    @Override
    public boolean lock(String lockName) throws LockAcquisitionException {
        return lock.lock(getPrefixedLockName(lockName));
    }

    @Override
    public boolean lock(String lockName, int seconds) throws LockAcquisitionException {
        return lock.lock(getPrefixedLockName(lockName), seconds);
    }

    @Override
    public boolean lock(String lockName, int value, TimeUnit unit) throws LockAcquisitionException {
        return lock.lock(getPrefixedLockName(lockName), value, unit);
    }

    @Override
    public boolean unlock(String lockName) throws LockReleaseException {
        return lock.unlock(getPrefixedLockName(lockName));
    }

    @Override
    public ILock getTypedLock(String prefix) {
        throw new UnsupportedOperationException();
    }

    @Override
    public LockObject getNamedLock(String lockName) {
        throw new UnsupportedOperationException();
    }

}
