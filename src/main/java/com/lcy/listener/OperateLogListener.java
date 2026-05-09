package com.lcy.listener;

import com.lcy.config.RabbitMQConfig;
import com.lcy.pojo.OperateLog;
import com.lcy.service.OperateLogService;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OperateLogListener {

    @Autowired
    private OperateLogService operateLogService;

    @RabbitListener(queues = RabbitMQConfig.OPERATE_LOG_QUEUE)
    public void handleOperateLog(OperateLog operateLog, Channel channel,
                                  @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) {
        try {
            log.info("接收到操作日志: {}", operateLog);
            operateLogService.insert(operateLog);
            channel.basicAck(deliveryTag, false);
            log.info("操作日志保存成功");
        } catch (Exception e) {
            log.error("操作日志保存失败: {}", e.getMessage());
            try {
                channel.basicNack(deliveryTag, false, false);
            } catch (Exception ex) {
                log.error("消息拒绝失败", ex);
            }
        }
    }
}
