package com.quan.boot.config;


import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
/**
 * 声明4中交换机类型
 */
public class RabbitMQConfig {

    /**
     * direct 交换机
     * 1.声明交换机
     * 2.声明队列
     * 3.绑定交换机与队列
     */

//
    public static final String DIRECT_EXCHANGE = "direct.exchange";
    public static final String DIRECT_QUEUE = "direct.queue";
    public static final String DIRECT_ROUTING_KEY = "direct.routing.key";

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(DIRECT_EXCHANGE, true, false);
    }

    @Bean
    public Queue directQueue() {
        return new Queue(DIRECT_QUEUE,true,false,false);
    }

    @Bean
    public Binding directBinding() {
        return BindingBuilder
                .bind(directQueue())
                .to(directExchange())
                .with(DIRECT_ROUTING_KEY);
    }


    /**
     * fanout 广播交换机
     */
    public static final String FANOUT_EXCHANGE = "fanout.exchange";
    public static final String FANOUT_QUEUE1 = "fanout.queue1";
    public static final String FANOUT_QUEUE2 = "fanout.queue2";

//   声明交换机
    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange(FANOUT_EXCHANGE, true, false);
    }

//    声明队列
    @Bean
    public Queue fanoutQueue1() {
        return new Queue(FANOUT_QUEUE1,true,false,false);
    }

    @Bean
    public Queue fanoutQueue2() {
        return new Queue(FANOUT_QUEUE2,true,false,false);
    }
//    队列绑定交换机
    @Bean
    public Declarables declarables() {
//        new Declarables(
//
//        )
        // 参数1: destination - 目标名称 （队列）（交换机）
        // 参数2: destinationType - 目标类型 队列绑定到交换机 | 交换机绑定到交换机
        // 参数3: exchange - 交换机名称
        // 参数4: routingKey - 路由键 (fanout模式下，路由键会被忽略)
        // 参数5: arguments - 扩展参数
        return new Declarables(
                new Binding(FANOUT_QUEUE1, Binding.DestinationType.QUEUE, FANOUT_EXCHANGE, "", null),
                new Binding(FANOUT_QUEUE2, Binding.DestinationType.QUEUE, FANOUT_EXCHANGE, "", null)
        );

    }

    /**
     * 使用topic 模式的队列
     *
     */

    /**
     * 订单主题交换机
     */
    public static final String TOPIC_EXCHANGE = "order.topic.exchange";
    /**
     * 创建订单队列
     */
    public static final String TOPIC_QUEUE = "order.create.queue";
    /**
     * 订单创建路由键
     */
    public static final String TOPIC_ROUTING_KEY = "order.#";


    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(TOPIC_EXCHANGE, true, false);
    }

    @Bean
    public Queue orderCreateQueue() {
        return new Queue(TOPIC_QUEUE,true,false,false);
    }

    @Bean
    public Binding orderCreateBinding() {
        return BindingBuilder
                .bind(orderCreateQueue())
                .to(topicExchange())
                .with(TOPIC_ROUTING_KEY);
    }

}
