package com.quan.rabbitmq.subscribe;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class EmitLog {


    private static final String EXCHANGE_NAME = "logs";


    public static void main(String[] args) throws IOException, TimeoutException {
//        创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
//      创建一个连接
        Connection connection = factory.newConnection();
//       创建通道
        Channel channel = connection.createChannel();
//        声明一个交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT,true);

        for (int i = 0; i < 10; i++) {
            String message = "这是一条广播的消息:" + i + "!";
            channel.basicPublish(EXCHANGE_NAME,"",null,message.getBytes());
            System.out.println(" [x] Sent '" + message + "'");
        }
    }

}

