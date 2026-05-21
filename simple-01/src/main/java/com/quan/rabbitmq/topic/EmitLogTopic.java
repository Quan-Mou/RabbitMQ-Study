package com.quan.rabbitmq.topic;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class EmitLogTopic {

    private static final String EXCHANGE_NAME = "topic_logs";

    private static final String  ROUTING_KEY1 = "beijing.weather";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC,true);

        String msg = "2026-5-19 北京天气多云";
        channel.basicPublish(EXCHANGE_NAME,ROUTING_KEY1,null,msg.getBytes());
        System.out.println("生产者发送了一条消息:" + msg);

        channel.close();
        connection.close();
    }



}
