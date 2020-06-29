package com.demo.config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.MessageHandler;

/**
 * 接收消息父类
 */
@Configuration
public abstract class MqttReceive {

    @Autowired
    private MqttReceiveConfig mqttReceiveConfig;

    //通道订阅handler
    public boolean MessageHandler(MessageHandler messageHandler){
       return mqttReceiveConfig.mqttInputChannel().subscribe(messageHandler);
    }

    //消息接收
    public abstract MessageHandler receiveMsgHandler();
}

