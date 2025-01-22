package com.zhouzhou.cloud.taskservice.job;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.xxl.job.core.log.XxlJobLogger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OrderTimeoutCancelJob {

    @XxlJob("cancelTimeOutOrder")
    public ReturnT<?> cancelTimeOutOrder(String param) {
        try {
            // 业务逻辑处理
            log.info("执行订单超时取消任务，参数：" + param);
            XxlJobLogger.log("执行订单超时取消任务，参数：" + param);

            // 执行取消超时订单的业务逻辑

            return new ReturnT<>(ReturnT.SUCCESS_CODE, "执行成功！");
        } catch (Exception e) {
            XxlJobLogger.log("订单超时取消任务执行失败：" + e.getMessage());
            return new ReturnT<>(ReturnT.FAIL_CODE, "执行失败：" + e.getMessage());
        }
    }
}
