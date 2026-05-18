package com.quan.rabbitmq.subscribe;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ReceiveLogs {


    private static final String EXCHANGE_NAME = "logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        // 为什么消费者也要声明交换机？
        // - 保证交换机一定存在（如果生产者还没启动，消费者先启动）
        // - exchangeDeclare 是幂等的：交换机已存在且类型一致时，无效果
        // - 建议生产者和消费者都声明，保证顺序无关性
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT,true);

        // 声明一个默认的队列，用于存放交换发过来的消息
        String queueName = channel.queueDeclare().getQueue();
        // ========== 6. 绑定：交换机 → 队列 ==========
        // queueBind(String queue, String exchange, String routingKey)
        //
        // 参数：
        // - queue: 队列名称，指定哪个队列要接收消息
        //
        // - exchange: 交换机名称，指定从哪个交换机接收消息
        //
        // - routingKey: 路由键
        //               * Fanout 模式下，这个参数被完全忽略，可以传空字符串 ""
        //               * 因为 Fanout 交换机会把消息广播给所有绑定的队列
        //               * 不管 routingKey 是什么，所有绑定的队列都能收到
        channel.queueBind(queueName, EXCHANGE_NAME, "");

        channel.basicConsume(queueName, new DeliverCallback() {
            @Override
            public void handle(String consumerTag, Delivery message) throws IOException {
                String msg = new String(message.getBody(), "UTF-8");
                System.out.println(" [x] Received '" + msg + "'");
            }
        }, new CancelCallback() {
            @Override
            public void handle(String consumerTag) throws IOException {

            }
        });

    }

}
