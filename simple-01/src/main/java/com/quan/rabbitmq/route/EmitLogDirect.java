package com.quan.rabbitmq.route;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class EmitLogDirect {


    private static final String EXCHANGE_NAME = "direct";
    private static final String ROUTING_KEY = "error";

    public static void main(String[] args) throws IOException, TimeoutException {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT,true);

        String message = "Hello World!";

        channel.basicPublish(EXCHANGE_NAME,ROUTING_KEY,null,message.getBytes());


        channel.close();
        connection.close();



    }


}
