package com.zhouzhou.cloud.common.systemreboot.service;

import com.zhouzhou.cloud.common.systemreboot.dto.InterruptMetadataDTO;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class InterruptRecoveryRunner implements ApplicationRunner {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    private final ApplicationContext applicationContext;

    @Override
    public void run(ApplicationArguments args) {
        Set<String> keys = redisTemplate.keys("interrupt:*");
        if (keys == null) return;

        for (String key : keys) {
            try {
                InterruptMetadataDTO metadata = (InterruptMetadataDTO) redisTemplate.opsForValue().get(key);
                if (metadata == null) continue;

                Class<?> targetClass = Class.forName(metadata.getClassName());
                Object bean = applicationContext.getBean(targetClass);

                Method method = targetClass.getMethod(metadata.getMethodName(), metadata.getParamTypes());
                method.invoke(bean, metadata.getArgs());

                redisTemplate.delete(key);
                log.info("恢复执行任务 [{}] 成功", key);
            } catch (Exception e) {
                log.error("恢复执行任务 [{}] 失败", key, e);
            }
        }
    }
}

