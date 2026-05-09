package com.lcy.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String OPERATE_LOG_EXCHANGE = "operate.log.exchange";
    public static final String OPERATE_LOG_QUEUE = "operate.log.queue";
    public static final String OPERATE_LOG_ROUTING_KEY = "operate.log";

    public static final String EMAIL_EXCHANGE = "email.exchange";
    public static final String EMAIL_QUEUE = "email.queue";
    public static final String EMAIL_ROUTING_KEY = "email.send";

    public static final String DATA_SYNC_EXCHANGE = "data.sync.exchange";
    public static final String DATA_SYNC_QUEUE = "data.sync.queue";
    public static final String DATA_SYNC_ROUTING_KEY = "data.sync";

    public static final String DEAD_LETTER_EXCHANGE = "dead.letter.exchange";
    public static final String DEAD_LETTER_QUEUE = "dead.letter.queue";
    public static final String DEAD_LETTER_ROUTING_KEY = "dead.letter";

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter messageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        rabbitTemplate.setMandatory(true);
        return rabbitTemplate;
    }
    //定义交换机
    @Bean
    public DirectExchange operateLogExchange() {
        return ExchangeBuilder
                .directExchange(OPERATE_LOG_EXCHANGE)
                .durable(true)//持久化：服务重启不丢失
                .build();
    }
    //定义队列
    @Bean
    public Queue operateLogQueue() {
        return QueueBuilder
                .durable(OPERATE_LOG_QUEUE)//持久化：服务重启不丢失
                .withArgument("x-dead-letter-exchange", DEAD_LETTER_EXCHANGE)//死信交换机
                .withArgument("x-dead-letter-routing-key", DEAD_LETTER_ROUTING_KEY)//死信路由键
                .build();
    }
    //定义绑定关系，把队列和交换机绑定起来
    @Bean
    public Binding operateLogBinding() {
        return BindingBuilder
                .bind(operateLogQueue())
                .to(operateLogExchange())
                .with(OPERATE_LOG_ROUTING_KEY);
    }

    @Bean
    public DirectExchange emailExchange() {
        return ExchangeBuilder
                .directExchange(EMAIL_EXCHANGE)
                .durable(true)
                .build();
    }

    @Bean
    public Queue emailQueue() {
        return QueueBuilder
                .durable(EMAIL_QUEUE)
                .withArgument("x-dead-letter-exchange", DEAD_LETTER_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", DEAD_LETTER_ROUTING_KEY)
                .build();
    }

    @Bean
    public Binding emailBinding() {
        return BindingBuilder
                .bind(emailQueue())
                .to(emailExchange())
                .with(EMAIL_ROUTING_KEY);
    }

    @Bean
    public DirectExchange dataSyncExchange() {
        return ExchangeBuilder
                .directExchange(DATA_SYNC_EXCHANGE)
                .durable(true)
                .build();
    }

    @Bean
    public Queue dataSyncQueue() {
        return QueueBuilder
                .durable(DATA_SYNC_QUEUE)
                .withArgument("x-dead-letter-exchange", DEAD_LETTER_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", DEAD_LETTER_ROUTING_KEY)
                .build();
    }

    @Bean
    public Binding dataSyncBinding() {
        return BindingBuilder
                .bind(dataSyncQueue())
                .to(dataSyncExchange())
                .with(DATA_SYNC_ROUTING_KEY);
    }

    @Bean
    public DirectExchange deadLetterExchange() {
        return ExchangeBuilder
                .directExchange(DEAD_LETTER_EXCHANGE)
                .durable(true)
                .build();
    }

    @Bean
    public Queue deadLetterQueue() {
        return QueueBuilder
                .durable(DEAD_LETTER_QUEUE)
                .build();
    }

    @Bean
    public Binding deadLetterBinding() {
        return BindingBuilder
                .bind(deadLetterQueue())
                .to(deadLetterExchange())
                .with(DEAD_LETTER_ROUTING_KEY);
    }
}
