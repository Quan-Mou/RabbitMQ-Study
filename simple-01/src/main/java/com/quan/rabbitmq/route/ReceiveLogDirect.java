package com.quan.rabbitmq.route;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ReceiveLogDirect {

    private static final String EXCHANGE_NAME = "direct";
    private static final String ROUTING_KEY = "error";
    private static final String QUEUE_NAME = "error.queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT,true);
        channel.queueDeclare(QUEUE_NAME,true,false,false,null);

        channel.queueBind(QUEUE_NAME,EXCHANGE_NAME,ROUTING_KEY);

        channel.basicConsume(QUEUE_NAME, new DeliverCallback() {
            @Override
            public void handle(String consumerTag, Delivery message) throws IOException {
                String msg = new String(message.getBody(), "UTF-8");
                System.out.println(" [x] Received '" +
                        message.getEnvelope().getRoutingKey() + "':'" + msg + "'");
            }
        }, new CancelCallback() {
            @Override
            public void handle(String consumerTag) throws IOException {}
        });


    }


}
