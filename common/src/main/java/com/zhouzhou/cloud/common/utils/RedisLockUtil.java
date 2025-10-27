package com.zhouzhou.cloud.common.utils;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * REDIS锁
 *
 * @author sqr
 */
@Slf4j
@Component
public class RedisLockUtil {

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    /***
     * 加锁
     * @param key
     * @param value 当前时间+超时时间(超时时间最好设置在10秒以上,保证在不同的项目获取到的时间误差在控制范围内)
     * @return 锁住返回true
     */
    public boolean lock(String key, String value) {
        try {
            //setNX 返回boolean
            Boolean aBoolean = redisTemplate.opsForValue().setIfAbsent(key, value);
            if (Boolean.TRUE.equals(aBoolean)) {
                return true;
            }
            //如果锁超时 ***
            String currentValue = redisTemplate.opsForValue().get(key);
            if (!StringUtils.isEmpty(currentValue) && Long.parseLong(currentValue) < System.currentTimeMillis()) {
                //获取上一个锁的时间
                String oldvalue = redisTemplate.opsForValue().getAndSet(key, value);
                if (!StringUtils.isEmpty(oldvalue) && oldvalue.equals(currentValue)) {
                    return true;
                }
            }
        } catch (Exception e) {
            log.error("加锁发生异常[{}]", e.getLocalizedMessage(), e);
        }
        return false;
    }

    /***
     * 解锁
     * @param key
     * @param value
     * @return
     */
    public void unlock(String key, String value) {
        try {
            String currentValue = redisTemplate.opsForValue().get(key);
            if (StringUtils.isBlank(currentValue)) {
                return;
            }
            if (!StringUtils.isEmpty(currentValue) && currentValue.equals(value)) {
                redisTemplate.delete(key);
            }
        } catch (Exception e) {
            log.error("解锁异常[{}]", e.getLocalizedMessage(), e);
        }
    }
}
