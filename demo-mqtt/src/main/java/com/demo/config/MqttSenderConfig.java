package com.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.messaging.MessageChannel;

/**
 * 发送配置
 */
@Configuration
public class MqttSenderConfig {

    @Autowired
    private MqttReceiveConfig mqttReceiveConfig;

    @Bean
    @ServiceActivator(inputChannel = "mqttOutboundChannel")
    public MqttPahoMessageHandler mqttOutbound() {
        MqttPahoMessageHandler messageHandler =  new MqttPahoMessageHandler(mqttReceiveConfig.getClientId(), mqttReceiveConfig.mqttClientFactory());
        messageHandler.setAsync(true);
        messageHandler.setDefaultTopic(mqttReceiveConfig.getDefaultTopic());
        //messageHandler.connectionLost();   连接丢失处理
        return messageHandler;
    }

    /**
     * 通道
     * @return
     */
    @Bean
    public MessageChannel mqttOutboundChannel() {
        return new DirectChannel();
    }
}