package com.lpf.mqtt;

import com.lpf.model.MqttInfo;
import com.lpf.model.MqttMessage;
import com.lpf.service.MqttService;
import com.lpf.util.JsonUtil;
import com.lpf.util.SnowflakeIdWorker;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

import javax.annotation.Resource;
import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

@Configuration
@IntegrationComponentScan
public class MqttConfiguration {

    private static Logger log = LoggerFactory.getLogger(MqttConfiguration.class);

    private static SnowflakeIdWorker snowflakeIdWorker = new SnowflakeIdWorker(15,15);

    @Resource
    private MqttService mqttService;


    @Value("${spring.mqtt.topic}")
    private String topic;

    @Value("${spring.mqtt.url}")
    private String hostUrl;

    @Value("${spring.mqtt.username}")
    private String username;

    @Value("${spring.mqtt.password}")
    private String password;

    @Value("${spring.mqtt.clientId}")
    private String clientId;

    @Value(("${spring.mqtt.completionTimeout}"))
    private int completionTimeout;

    @Bean
    public MqttConnectOptions getReceiverMqttConnectOptions(){
        MqttConnectOptions mqttConnectOptions=new MqttConnectOptions();
        mqttConnectOptions.setCleanSession(true);
        mqttConnectOptions.setConnectionTimeout(10);
        mqttConnectOptions.setKeepAliveInterval(90);
        mqttConnectOptions.setAutomaticReconnect(true);
        mqttConnectOptions.setUserName(username);
        mqttConnectOptions.setPassword(password.toCharArray());
        mqttConnectOptions.setServerURIs(new String[]{hostUrl});
        mqttConnectOptions.setKeepAliveInterval(2);
        return mqttConnectOptions;
    }
    @Bean
    public MqttPahoClientFactory mqttClientFactory() {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        factory.setConnectionOptions(getReceiverMqttConnectOptions());
        return factory;
    }

    //接收通道
    @Bean
    public MessageChannel mqttInputChannel() {
        return new DirectChannel();
    }

    //配置client,监听的topic
    @Bean
    public MessageProducer inbound() {
        String id = clientId + System.currentTimeMillis();
        MqttPahoMessageDrivenChannelAdapter adapter =
                new MqttPahoMessageDrivenChannelAdapter(id, mqttClientFactory(),
                        topic);
        adapter.setCompletionTimeout(completionTimeout);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(1);
        adapter.setOutputChannel(mqttInputChannel());
        return adapter;
    }

    //通过通道获取数据
    @Bean
    @ServiceActivator(inputChannel = "mqttInputChannel")
    public MessageHandler handler() {
        return new MessageHandler() {
            @Override
            public void handleMessage(Message<?> message) throws MessagingException {
                log.info("消息体：{}", message);
//                log.info("主题：{}，消息接收到的数据：{}", message.getHeaders().get("mqtt_receivedTopic"), message.getPayload());

                MqttMessage mqttMessage = new MqttMessage();
                mqttMessage.setMqttId(snowflakeIdWorker.nextId());
                mqttMessage.setTopic(message.getHeaders().get("mqtt_receivedTopic").toString());
                mqttMessage.setMessage(message.getPayload().toString());
                mqttMessage.setMqttQos(Integer.parseInt(message.getHeaders().get("mqtt_receivedQos").toString()));
                Integer retained = message.getHeaders().get("mqtt_receivedRetained").equals("false")?0:1;
                mqttMessage.setMqttReceivedRetained(retained);
                mqttMessage.setMqttTime(new Date());

                Callable<Void> myCallable = new Callable<Void>() {
                    @Override
                    public Void call() throws Exception {
                        dealWithMqttMessage(mqttMessage);
                        return null;
                    }
                };
                FutureTask<Void> task = new FutureTask<>(myCallable);
                Thread t = new Thread(task);
                t.start();

                //插入mqtt历史记录
                mqttService.insertMqttMessage(mqttMessage);
            }
        };
    }

    //处理mqtt数据
    public void dealWithMqttMessage(MqttMessage mqttMessage) {

        try {
            MqttInfo mqttInfo = JsonUtil.objectFromJsonStr(mqttMessage.getMessage(), MqttInfo.class);
            switch (mqttInfo.getPrefix()) {
                case "123":
                    int i=0;
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Bean
    @ServiceActivator(inputChannel = "mqttOutboundChannel")
    public MessageHandler mqttOutbound() {
        String id = "convert" + System.currentTimeMillis();
        MqttPahoMessageHandler messageHandler =  new MqttPahoMessageHandler(id, mqttClientFactory());
        messageHandler.setAsync(true);
        messageHandler.setDefaultTopic(topic);
        return messageHandler;
    }
    @Bean
    public MessageChannel mqttOutboundChannel() {
        return new DirectChannel();
    }

}
