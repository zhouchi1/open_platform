package com.zhouzhou.cloud.gatewayservice.rabbitmqproducer;

public interface RabbitMqSenderApi {

    void sendSimpleMessage(String queueName, Object message);

    void sendRoutingMessage(String exchangeName, String routingKey, Object message);

    void sendBroadcastMessage(String exchangeName, Object message);

    void sendDelayedMessage(String exchangeName, String routingKey, Object message, int delay);

    void sendTopicMessage(String exchangeName, String routingKey, Object message);

}
