package com.demo.utils;

import com.demo.config.MqttReceive;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

@Log4j2
@Configuration
public class DefaultMqttReceive extends MqttReceive implements ApplicationListener<ContextRefreshedEvent> {

    /**
     * 通道注册当前消费Handler
     * @param contextRefreshedEvent
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        boolean isSubscribe = super.MessageHandler(this.receiveMsgHandler());

        if (isSubscribe){
            log.info("注册成功");
        } else {
            log.info("注册失败");
        }
    }

    /**
     * 消息处理
     * @return
     */
    @Override
    public MessageHandler receiveMsgHandler() {
        return new MessageHandler() {
            @Override
            public void handleMessage(Message<?> message) throws MessagingException {
                log.info("消息：" + message.getPayload().toString());
                System.out.println();
                String topic = message.getHeaders().get("mqtt_receivedTopic").toString();
                log.info("主题" + topic);
                String type = topic.substring(topic.lastIndexOf("/") + 1, topic.length());
                System.out.println(type);
            }
        };
    }
}
