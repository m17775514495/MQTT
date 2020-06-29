package com.demo;

import com.demo.utils.MqttGateways;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DemoApplicationTests {

    @Autowired
    private MqttGateways mqttGateways;

    @Test
    public void sendMqtt(){
        mqttGateways.sendToMqtt("测试测试","/test1");
    }
}
