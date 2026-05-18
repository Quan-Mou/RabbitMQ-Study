package com.quan.rabbitmq.workQueues;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.util.Map;

public class Worker1 {

  private final static String QUEUE_NAME = "hello";

  public static void main(String[] argv) throws Exception {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("localhost");
    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();

    Map<String, Object> args = Map.of("x-queue-type", "quorum");

      /**
       * 声明队列，生产者和消费者声明的队列一致才可以正常消费
       * 参数1：队列名
       * 参数2：持久化
       * 参数3：独占
       * 参数4：自动喊出
       * 参数5：参数
       */
    channel.queueDeclare(QUEUE_NAME, true, false, false, args);
    System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

    channel.basicQos(1); // 表示一次先工作进程发送一条消息，在上一条消息没有处理完不要先它发送新消息

      /**
       * 接受到消息的回调
       * 参数1:消费者标签（唯一标识，一般有Rabbitmq自动生成）
       * 参数2：消息对象，包含消息体、消息属性、路由信息等
       */
      DeliverCallback deliverCallback = (consumerTag, delivery) -> {
          String message = new String(delivery.getBody(), "UTF-8");
          System.out.println(" [x] Received '" + message + "'");
          channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
          try {
              Thread.sleep(2000);
          } catch (InterruptedException e) {
              throw new RuntimeException(e);
          }
      };

//    开始消费（队列名、自动确认、回调、取消回调）
      /**
       * 参数1：要消费的队列名
       * 参数2：自动确认，true：消息到达消费者后，RabbitMQ 立即标记为已消费（从队列删除）false: 需要手动调用 channel.basicAck() 确认
       * 参数3：收到消息时的回调函数
       * 参数4：取消消费时的回调函数 当消费者被取消（如队列被删除）时触发
       */
      channel.basicConsume(QUEUE_NAME, false, deliverCallback, consumerTag -> {

      });
  }
}
