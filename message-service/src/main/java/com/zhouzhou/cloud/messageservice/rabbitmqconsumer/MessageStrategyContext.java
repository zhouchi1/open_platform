package com.zhouzhou.cloud.messageservice.rabbitmqconsumer;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class MessageStrategyContext implements InitializingBean {

    @Resource
    private List<MessageProcessingStrategy> strategies;

    private final Map<String, MessageProcessingStrategy> strategyMap = new HashMap<>();

    @Override
    public void afterPropertiesSet() {
        for (MessageProcessingStrategy strategy : strategies) {
            String beanName = strategy.getClass().getSimpleName();
            String type = beanName.replace("Strategy", "").toUpperCase();
            strategyMap.put(type, strategy);
            log.info("Registered message processing strategy: {} for message type: {}", beanName, type);
        }
    }

    public void execute(Message message) throws Exception {
        MessageProcessingStrategy strategy = strategyMap.get(message.getMessageProperties().getType());
        if (strategy != null) {
            strategy.processMessage(message);
        } else {
            log.error("未找到消息类型的处理器：" + message.getMessageProperties().getType());
        }
    }
}
