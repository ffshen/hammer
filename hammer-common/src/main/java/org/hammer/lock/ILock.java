package org.hammer.lock;

import java.util.concurrent.TimeUnit;

/**
 * 锁接口
 * <p>
 * Created by neron.liu on 8/25/16.
 */
public interface ILock {

    /**
     * @param lockName
     * @return
     */
    public boolean exists(String lockName);

    /**
     * @param lockName 锁的名称
     * @return
     */
    public boolean lock(String lockName) throws LockAcquisitionException;

    /**
     * @param lockName 锁的名称
     * @param seconds  锁的失效时间(秒)
     * @return
     */
    public boolean lock(String lockName, int seconds) throws LockAcquisitionException;

    /**
     * @param lockName 锁的名称
     * @param value    锁的失效时间
     * @param unit     时间单位
     * @return
     */
    public boolean lock(String lockName, int value, TimeUnit unit) throws LockAcquisitionException;

    /**
     * @param lockName
     * @return
     */
    public boolean unlock(String lockName) throws LockReleaseException;

    /**
     * 类型锁, 前缀相同的锁, 为类型锁
     * 如LCK_{SKU1}, LCK_{SKU2}, LCK_{SKU3}, 操作时只需传入{SKU1}
     *
     * @param prefix
     * @return
     */
    public ILock getTypedLock(String prefix);

    /**
     * 对象锁, 使用名字创建一个锁对象, 直接操作
     *
     * @param lockName
     * @return
     */
    public LockObject getNamedLock(String lockName);

}
