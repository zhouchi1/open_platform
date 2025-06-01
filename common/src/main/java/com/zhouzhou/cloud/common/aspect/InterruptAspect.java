package com.zhouzhou.cloud.common.aspect;

import com.alibaba.fastjson2.JSON;
import com.zhouzhou.cloud.common.annotation.InterruptRestore;
import com.zhouzhou.cloud.common.dto.InterruptMetadataDTO;
import com.zhouzhou.cloud.common.dto.ParamInfoDTO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
    private RedisTemplate<String, String> redisTemplate;


    /**
     * 捕获在此方法执行时的方法元数据放到Redis中存储
     * （1）在发生系统中断异常时 不删除Redis存储
     * （2）在发生其他的异常时/执行成功 则删除Redis存储的元数据信息
     * 在程序开机启动时 查询Redis中携带扫描携带注解的所有方法 尝试使用数据重新进行方法的执行
     */
    @Around("@annotation(annotation)")
    public Object around(ProceedingJoinPoint joinPoint,
                         InterruptRestore annotation) throws Throwable {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();

        Object[] args = joinPoint.getArgs();

        // 构造任务信息
        String taskId = UUID.randomUUID().toString();
        InterruptMetadataDTO task = new InterruptMetadataDTO(
                method.getDeclaringClass().getName(),
                method.getName(),
                annotation.name(),
                method.getParameterTypes(),
                serializeArgs(args)
        );

        // 存储执行任务信息到Redis中
        redisTemplate.opsForHash().put("recovery:tasks", taskId, JSON.toJSONString(task));

        boolean interrupted = false;
        try {
            // 1. 执行目标方法
            return joinPoint.proceed();
        } catch (InterruptedException ie) {
            // 2. 线程中断：保留任务，继续抛中断
            interrupted = true;
            throw ie;
        } catch (Throwable t) {
            // 3. 其他异常：删除任务，再包装为 BizException
            redisTemplate.opsForHash().delete("recovery:tasks", taskId);
            throw new Exception("任务执行失败",t);
        } finally {
            // 4. 正常返回（或遇中断）且非中断分支，都要删除任务
            if (!interrupted) {
                redisTemplate.opsForHash().delete("recovery:tasks", taskId);
            }
        }
    }

    private List<ParamInfoDTO> serializeArgs(Object[] args) {
        List<ParamInfoDTO> params = new ArrayList<>();
        for (Object arg : args) {
            String jsonValue = JSON.toJSONString(arg);
            params.add(new ParamInfoDTO(jsonValue));
        }
        return params;
    }
}
