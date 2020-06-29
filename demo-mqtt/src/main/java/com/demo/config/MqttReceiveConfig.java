package com.demo.config;


import lombok.Data;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;


@Configuration
@IntegrationComponentScan
@PropertySource("classpath:mqtt.properties")
@Data
public class MqttReceiveConfig {

    @Value("${spring.mqtt.username}")
    private String username;

    @Value("${spring.mqtt.password}")
    private String password;

    @Value("${spring.mqtt.url}")
    private String hostUrl;

    @Value("${spring.mqtt.client.id}")
    private String clientId;

    @Value("${spring.mqtt.default.topic}")
    private String defaultTopic;

    //连接超时
    @Value("${spring.mqtt.completionTimeout}")
    private int completionTimeout ;

    //qos
    @Value("${spring.mqtt.qos}")
    private int qos ;

    //心跳检测时间
    @Value("${spring.mqtt.keepAliveInterval}")
    private int keepAliveInterval ;

    //adapter
    private MqttPahoMessageDrivenChannelAdapter adapter;

    /**
     * 设置连接属性
     * @return
     */
    @Bean
    public MqttConnectOptions getMqttConnectOptions(){
        System.out.println(username);
        MqttConnectOptions mqttConnectOptions=new MqttConnectOptions();
        mqttConnectOptions.setUserName(username);
        mqttConnectOptions.setPassword(password.toCharArray());
        mqttConnectOptions.setServerURIs(new String[]{hostUrl});
        mqttConnectOptions.setKeepAliveInterval(keepAliveInterval);

        return mqttConnectOptions;
    }

    /**
     * 客户端工厂
     * @return
     */
    @Bean
    public MqttPahoClientFactory mqttClientFactory() {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        factory.setConnectionOptions(getMqttConnectOptions());
        return factory;
    }

    /**
     * 接收通道
     * @return
     */
    @Bean
    public DirectChannel mqttInputChannel() {
        DirectChannel directChannel = new DirectChannel();
        return directChannel;
    }

    /**
     * 配置client,监听的topic
     */
   @Bean
   public MessageProducer inbound() {
       MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(clientId +"_inbound",
                                                                                              mqttClientFactory());
       adapter.addTopic(defaultTopic);
       adapter.setCompletionTimeout(completionTimeout);
       adapter.setConverter(new DefaultPahoMessageConverter());
       adapter.setQos(qos);
       adapter.setOutputChannel(mqttInputChannel());
       return adapter;
   }
}