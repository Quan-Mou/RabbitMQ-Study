package com.quan.boot.mq;


import com.quan.boot.config.RabbitMQConfig;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
public class MQHandler {

    /**
     * 默认交换机
     * @param msg
     */
//    @RabbitListener(bindings = @QueueBinding(
//            value = @Queue(value = "spring-boot-default.queue",durable = "false"),
//            exchange = @Exchange(type = ExchangeTypes.DIRECT)) //
//    )

//    @RabbitListener(bindings = @QueueBinding(
//            value = @Queue(value = "spring-boot-default.queue", durable = "false"),
//            exchange = @Exchange(value = "demo", type = ExchangeTypes.DIRECT)
//
//    ))
    @RabbitListener(queues = RabbitMQConfig.DIRECT_QUEUE)
    public void handlerDefaultMsg(String msg) {
        System.out.println("消费默认消息：" + msg);
    }

    @RabbitListener(queues = {RabbitMQConfig.FANOUT_QUEUE1, RabbitMQConfig.FANOUT_QUEUE2})
    public void handlerFanoutMsg(String msg,                                    // 消息体
                                 Channel channel,                               // RabbitMQ 通道
                                 @Header(AmqpHeaders.DELIVERY_TAG) long tag,    // 投递标签（手动确认用）
                                 @Header(AmqpHeaders.CONSUMER_QUEUE) String queueName  // 来源队列
                                )
    {

        System.out.println("来自队列： "+ queueName+", 消费默认消息：" + msg);
    }


    @RabbitListener(queues = RabbitMQConfig.TOPIC_QUEUE)
    public void handlerTopicMsg(
            String msg,
            @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String routingKey,
            @Header(AmqpHeaders.CONSUMER_QUEUE) String queueName
            ) {
        System.out.println("消费了来自队列" + queueName + "的消息：" + msg + "，路由健为" + routingKey);
    }







}
