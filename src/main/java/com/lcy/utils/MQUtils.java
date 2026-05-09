package com.lcy.utils;

import com.lcy.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MQUtils {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendOperateLog(Object log) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.OPERATE_LOG_EXCHANGE,
                RabbitMQConfig.OPERATE_LOG_ROUTING_KEY,
                log
        );
    }

    public void sendEmail(Object emailInfo) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EMAIL_EXCHANGE,
                RabbitMQConfig.EMAIL_ROUTING_KEY,
                emailInfo
        );
    }

    public void sendDataSync(Object data) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.DATA_SYNC_EXCHANGE,
                RabbitMQConfig.DATA_SYNC_ROUTING_KEY,
                data
        );
    }

    public void send(String exchange, String routingKey, Object message) {
        rabbitTemplate.convertAndSend(exchange, routingKey, message);
    }
}
