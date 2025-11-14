package com.zhouzhou.cloud.taskservice.job;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OrderTimeoutCancelJob {

    /**
     * 处理超时订单
     * @param param 请求参数
     * @return 定时任务执行结果
     */
    @XxlJob("cancelTimeOutOrder")
    public ReturnT<String> cancelTimeOutOrder(String param) {
        try {
            // 业务逻辑处理
            log.info("执行订单超时取消任务，参数：" + param);
            XxlJobHelper.log("执行订单超时取消任务，参数：" + param);

            // 执行取消超时订单的业务逻辑

            return new ReturnT<>(ReturnT.SUCCESS_CODE, "执行成功！");
        } catch (Exception e) {
            XxlJobHelper.log("订单超时取消任务执行失败：" + e.getMessage());
            return new ReturnT<>(ReturnT.FAIL_CODE, "执行失败：" + e.getMessage());
        }
    }

    /**
     * 处理websocket-Token过期的连接
     * @return 定时任务执行结果
     */
    @XxlJob("cancelTimeOutLink")
    public ReturnT<String> cancelTimeOutLink(String param){
        try {
            // 业务逻辑处理
            log.info("websocket连接过期token处理，参数：" + param);
            XxlJobHelper.log("websocket连接过期token处理，参数：" + param);

            // 处理websocket-Token过期的连接

            return new ReturnT<>(ReturnT.SUCCESS_CODE, "执行成功！");
        } catch (Exception e) {
            XxlJobHelper.log("websocket连接过期token处理：" + e.getMessage());
            return new ReturnT<>(ReturnT.FAIL_CODE, "websocket连接过期token处理 执行失败：" + e.getMessage());
        }
    }
}
