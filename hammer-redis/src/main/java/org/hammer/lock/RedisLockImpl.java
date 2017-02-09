package org.hammer.lock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisCluster;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static java.lang.String.format;
import static java.util.Optional.ofNullable;

/**
 * Created by neron.liu on 8/25/16.
 */
@Component
public class RedisLockImpl implements ILock {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static final String LOCKED_STATUS = "a_lock";

    private static final Long INVALID_STATUS = -1L;

    private static final Long SUCCESS_STATUS = 1L;

    @Autowired
    private JedisCluster jedis;

    @Override
    public boolean exists(String lockName) {
        try {
            return jedis.exists(lockName);
        } catch (Exception e) {
            logger.error("Error happened while checking lock {} exists", lockName, e);
            return false;
        }
    }

    @Override
    public boolean lock(String lockName) throws LockAcquisitionException {
        return lock(lockName, 0, TimeUnit.SECONDS);
    }

    @Override
    public boolean lock(String lockName, int seconds) throws LockAcquisitionException {
        return lock(lockName, seconds, TimeUnit.SECONDS);
    }

    @Override
    public boolean lock(String lockName, int value, TimeUnit unit) throws LockAcquisitionException {
        try {
            // 防止Redis返回异常的状态码, 导致程序崩溃
            Long status = ofNullable(jedis.setnx(lockName, LOCKED_STATUS)).orElse(INVALID_STATUS);

            if (Objects.equals(status, INVALID_STATUS)) {
                throw new LockAcquisitionException("Acquiring lock failed, Redis cluster was working abnormally! lockName=" + lockName);
            }

            if (Objects.equals(status, SUCCESS_STATUS)) {
                if (value > 0) {
                    jedis.expire(lockName, (int) unit.toSeconds(value));
                    logger.info("Lock '{}' acquired successfully! It will be expired after {} seconds.", lockName, value);
                } else {
                    logger.info("Lock '{}' acquired successfully!", lockName);
                }
                return true;
            } else {
                return false;
            }
        } catch (LockAcquisitionException ae) {
            throw ae;
        } catch (Exception e) {
            throw new LockAcquisitionException(format("Error happened while acquiring lock '%s'!", lockName), e);
        }
    }

    @Override
    public boolean unlock(String lockName) throws LockReleaseException {
        try {
            if (jedis.exists(lockName)) {
                jedis.del(lockName);
                logger.debug("Lock {} released successfully!");
                return true;
            } else {
                logger.warn("Trying to release an inexistent lock! lockName={}", lockName);
                return false;
            }
        } catch (Exception e) {
            throw new LockReleaseException(format("Error happened while releasing lock %s!", lockName), e);
        }
    }

    @Override
    public ILock getTypedLock(String prefix) {
        return new PrefixedLock(prefix, this);
    }

    @Override
    public LockObject getNamedLock(String lockName) {
        return new NamedLock(lockName, this);
    }

}
