package cn.iocoder.yudao.framework.redis.core.utils;

import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.common.exception.enums.GlobalErrorCodeConstants;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;


@SuppressWarnings("unchecked")
public class RedisUtil {

    /**
     * 默认缓存过期时间300s
     */
    private static final long DEFAULT_EXPIRE_TIME = 300L;


    private static RedisTemplate<String, Object> redisTemplate;

    public static void init(RedisTemplate<String, Object> redisTemplate){
        RedisUtil.redisTemplate = redisTemplate;
    }


    public static  <T> T get(String key) {
        if (StrUtil.isBlank(key)) {
            return null;
        }
        return (T) redisTemplate.opsForValue().get(key);
    }

    public static <T> T get(String format, String key) {
        if (StrUtil.isBlank(key)) {
            return null;
        }
        String redisKey = String.format(format, key);
        return (T) redisTemplate.opsForValue().get(redisKey);
    }

    public static boolean del(String redisKey) {
        if (StrUtil.isBlank(redisKey)) {
            return true;
        }
        return Boolean.TRUE.equals(redisTemplate.delete(redisKey));
    }

    public static boolean del(String format, String key) {
        if (StrUtil.isBlank(key)) {
            return true;
        }
        return del(String.format(format, key));
    }

    public static void set(String key, Object value) {
        verifyKey(key);
        set(key, value, DEFAULT_EXPIRE_TIME, TimeUnit.MINUTES);
    }

    public static void set(String format, String key, Object value, Long expireTime, TimeUnit timeUnit) {
        verifyKey(key);
        set(String.format(format, key), value, expireTime, timeUnit);
    }

    public static void set(String key, Object value, Long expireTime, TimeUnit timeUnit) {
        verifyKey(key);
        redisTemplate.opsForValue().set(key, value, expireTime, timeUnit);
    }

    public static void setIfPresent(String format, String key, Object value, Long expireTime, TimeUnit timeUnit) {
        verifyKey(key);
        setIfPresent(String.format(format, key), value, expireTime, timeUnit);
    }

    public static void setIfPresent(String key, Object value, Long expireTime, TimeUnit timeUnit) {
        verifyKey(key);
        redisTemplate.opsForValue().setIfPresent(key, value, expireTime, timeUnit);
    }


    private static void verifyKey(String key) {
        if (StrUtil.isBlank(key)) {
            throw exception(GlobalErrorCodeConstants.PARAMETER_ERROR, "redis key is blank");
        }
    }
}
