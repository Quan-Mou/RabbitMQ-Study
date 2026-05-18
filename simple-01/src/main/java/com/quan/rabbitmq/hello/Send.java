package com.quan.rabbitmq.hello;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.Map;

public class Send {


  private final static String QUEUE_NAME = "hello";

  public static void main(String[] argv) throws Exception {
      ConnectionFactory factory = new ConnectionFactory();
      factory.setHost("localhost");
      try (Connection connection = factory.newConnection();


          Channel channel = connection.createChannel()) {
          Map<String, Object> args = Map.of("x-queue-type", "quorum");
          /**
           * 声明队列，生产者和消费者声明的队列一致才可以正常消费
           * 参数1：队列名 生产者与消费者必须使用相同名称
           * 参数2：（队列）持久化 true: 队列定义会保存到磁盘，RabbitMQ 重启后队列依然存在  false: 队列是临时的，RabbitMQ 重启后队列会消失
           * 参数3：独占 true: 队列只能被当前连接使用，连接关闭后队列自动删除 false: 多个消费者可以共享同一个队列
           * 参数4：自动删除 是否自动删除（true = 自动删除，false = 不删除）
           * 参数5：参数 扩展参数（Map），用于设置队列的额外属性 例如：消息过期时间、死信交换机、最大长度等
           */
          channel.queueDeclare(QUEUE_NAME, true, false, false, args);

          String message = "Hello World!！！！！";

          /**
           * 发送消息
           * 参数1：交换机名称，"" 就是默认交换机，默认交换机的routing-key就是队列名
           * 参数2：路由键，使用默认交换机时，routingKey必须等于队列名名，使用自定义交换机时，路由器根据routingKey路由消息
           * 参数3：消息属性（BasicProperties 对象）可以设置可以设置：消息持久化、过期时间、优先级、内容类型等，传null表示默认属性
           * 参数4：消息体
           */
          channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
          System.out.println(" [x] Sent '" + message + "'");
      }
  }
}