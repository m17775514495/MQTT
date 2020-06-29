package com.demo.controller;

import com.demo.utils.MqttGateways;
import com.demo.pojo.MqttVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/mqtt")
public class TestController {

    @Autowired
    private MqttGateways mqttGateways;

    @RequestMapping(value = "/test", method = RequestMethod.POST)
    @ResponseBody
    public void test(@ModelAttribute MqttVo mqttVo){
        mqttGateways.sendToMqtt(mqttVo.getMsg(), mqttVo.getTopic());
    }
}
