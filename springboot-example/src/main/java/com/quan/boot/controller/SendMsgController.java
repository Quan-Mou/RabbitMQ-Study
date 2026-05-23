package com.quan.boot.controller;

import com.quan.boot.config.RabbitMQConfig;
import jakarta.annotation.Resource;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SendMsgController {

    @Resource
    private RabbitTemplate rabbitTemplate;


    @GetMapping("sendDefault")
    public String sendMsg(String msg) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.DIRECT_EXCHANGE,RabbitMQConfig.DIRECT_ROUTING_KEY,msg);
        return "sendDefault 发送成功 ：" + msg ;
    }

    @GetMapping("sendFanout")
    public String sendFanout(String msg) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.FANOUT_EXCHANGE,"",msg);
        return "sendFanout 发送成功 ：" + msg ;
    }

    @GetMapping("sendTopic")
    public String sendTopic(String msg,String routingKey) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.TOPIC_EXCHANGE,routingKey,msg);
        return "sendTopic 发送成功 ：" + msg ;
    }

}
