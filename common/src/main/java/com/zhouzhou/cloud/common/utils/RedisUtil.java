package com.zhouzhou.cloud.common.utils;

import jakarta.annotation.Resource;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUtil {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /** -------------------通用操作--------------------- */

    /**
     * 设置缓存
     * @param key 键
     * @param value 值
     * @param timeout 超时时间（秒）
     */
    public void set(String key, Object value, long timeout) {
        try {
            ValueOperations<String, Object> ops = redisTemplate.opsForValue();
            if (timeout > 0) {
                ops.set(key, value, timeout, TimeUnit.SECONDS);
            } else {
                ops.set(key, value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取缓存
     * @param key 键
     * @return 值
     */
    public Object get(String key) {
        try {
            return redisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 删除缓存
     * @param key 键
     * @return 是否成功
     */
    public boolean delete(String key) {
        try {
            if (redisTemplate == null) {
                throw new NullPointerException("RedisTemplate is null");
            }
            Boolean expireResult = redisTemplate.delete(key);
            if (expireResult == null) {
                return false;
            }
            return expireResult;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 设置过期时间
     * @param key 键
     * @param timeout 超时时间（秒）
     * @return 是否成功
     */
    public boolean expire(String key, long timeout) {
        try {
            if (redisTemplate == null) {
                throw new NullPointerException("RedisTemplate is null");
            }
            if (timeout > 0) {
                Boolean expireResult = redisTemplate.expire(key, timeout, TimeUnit.SECONDS);
                if (expireResult == null) {
                    return false;
                }
                return expireResult;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获取过期时间
     * @param key 键
     * @return 剩余时间（秒），-1 表示永久有效
     */
    public long getExpire(String key) {
        try {
            if (redisTemplate == null) {
                throw new NullPointerException("RedisTemplate is null");
            }
            return redisTemplate.getExpire(key, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
            return -2;
        }
    }

    /**
     * 判断 key 是否存在
     * @param key 键
     * @return 是否存在
     */
    public boolean hasKey(String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /** -------------------String 类型操作--------------------- */

    /**
     * 递增
     * @param key 键
     * @param delta 增量（必须大于 0）
     * @return 递增后的值
     */
    public long increment(String key, long delta) {
        try {
            if (delta <= 0) {
                throw new IllegalArgumentException("Increment delta must be greater than 0");
            }
            return redisTemplate.opsForValue().increment(key, delta);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 递减
     * @param key 键
     * @param delta 减量（必须大于 0）
     * @return 递减后的值
     */
    public long decrement(String key, long delta) {
        try {
            if (delta <= 0) {
                throw new IllegalArgumentException("Decrement delta must be greater than 0");
            }
            return redisTemplate.opsForValue().increment(key, -delta);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /** -------------------Hash 类型操作--------------------- */

    /**
     * 设置 Hash 缓存
     * @param key 键
     * @param hashKey Hash 键
     * @param value 值
     */
    public void hSet(String key, String hashKey, String value) {
        try {
            redisTemplate.opsForHash().put(key, hashKey, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取 Hash 缓存
     * @param key 键
     * @param hashKey Hash 键
     * @return 值
     */
    public Object hGet(String key, String hashKey) {
        try {
            return redisTemplate.opsForHash().get(key, hashKey);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 删除 Hash 键
     * @param key 键
     * @param hashKeys Hash 键数组
     */
    public void hDelete(String key, Object... hashKeys) {
        try {
            redisTemplate.opsForHash().delete(key, hashKeys);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断 Hash 键是否存在
     * @param key 键
     * @param hashKey Hash 键
     * @return 是否存在
     */
    public boolean hHasKey(String key, String hashKey) {
        try {
            return redisTemplate.opsForHash().hasKey(key, hashKey);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /** -------------------Set 类型操作--------------------- */

    /**
     * 添加 Set 缓存
     * @param key 键
     * @param values 值数组
     * @return 添加的元素个数
     */
    public long sAdd(String key, Object... values) {
        try {
            return redisTemplate.opsForSet().add(key, values);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 获取 Set 缓存
     * @param key 键
     * @return 值集合
     */
    public Set<Object> sMembers(String key) {
        try {
            return redisTemplate.opsForSet().members(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 判断值是否在 Set 中
     * @param key 键
     * @param value 值
     * @return 是否存在
     */
    public boolean sIsMember(String key, Object value) {
        try {
            return redisTemplate.opsForSet().isMember(key, value);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /** -------------------List 类型操作--------------------- */

    /**
     * 向 List 右侧推入值
     * @param key 键
     * @param value 值
     * @return 推入后的列表长度
     */
    public long lPush(String key, Object value) {
        try {
            return redisTemplate.opsForList().rightPush(key, value);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 从 List 左侧弹出值
     * @param key 键
     * @return 弹出的值
     */
    public Object lPop(String key) {
        try {
            return redisTemplate.opsForList().leftPop(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取 List 范围内的值
     * @param key 键
     * @param start 起始索引
     * @param end 结束索引
     * @return 值集合
     */
    public List<Object> lRange(String key, long start, long end) {
        try {
            return redisTemplate.opsForList().range(key, start, end);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

