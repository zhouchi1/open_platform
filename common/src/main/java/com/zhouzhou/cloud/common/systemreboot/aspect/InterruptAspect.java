package com.zhouzhou.cloud.common.systemreboot.aspect;

import com.zhouzhou.cloud.common.systemreboot.aspect.annotation.InterruptRestoreAnnotation;
import com.zhouzhou.cloud.common.systemreboot.dto.InterruptMetadataDTO;
import com.zhouzhou.cloud.common.utils.RedisUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @Author: Sr.Zhou
 * @CreateTime: 2025-05-19
 * @Description: 程序中断数据处理自定义处理切面
 */
@Slf4j
@Aspect
@Component
public class InterruptAspect {

    @Resource
    private RedisUtil redisUtil;

    /**
     * 捕获在此方法执行时 出现的中断异常 将方法元数据存储 在开机启动时 扫描携带注解的所有方法 尝试试用数据重新进行方法的执行
     */
    @Around("@annotation(annotation)")
    public Object around(ProceedingJoinPoint joinPoint, InterruptRestoreAnnotation annotation) throws Throwable {
        String key = annotation.key();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        // 拼接元数据
        InterruptMetadataDTO metadata = new InterruptMetadataDTO(
                method.getDeclaringClass().getName(),
                method.getName(),
                signature.getParameterTypes(),
                joinPoint.getArgs()
        );

        try {
            Object result = joinPoint.proceed();
            redisUtil.delete("interrupt:" + key);
            return result;
        } catch (Throwable e) {
            // 保存中断信息
            redisUtil.set("interrupt:" + key, metadata, -1);
            log.error("任务 [{}] 执行失败，中断信息已记录", key, e);
            throw e;
        }
    }
}
