package com.example.demo.mq.rocketmq;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 生产者配置
 */
@Configuration
public class ProducerConfig {
    @Value("${rocketmq.producer.isOnOff}")
    private boolean isOnOff;
    @Value("${rocketmq.producer.groupName}")
    private String groupName;
    @Value("${rocketmq.producer.namesrvAddr}")
    private String namesrvAddr;
    @Value("${rocketmq.producer.maxMessageSize}")
    private Integer maxMessageSize ;
    @Value("${rocketmq.producer.sendMsgTimeout}")
    private Integer sendMsgTimeout;
    @Value("${rocketmq.producer.retryTimesWhenSendFailed}")
    private Integer retryTimesWhenSendFailed;
    @Bean
    public DefaultMQProducer defaultMQProducer() {
        DefaultMQProducer producer;
        producer = new DefaultMQProducer(this.groupName);
        producer.setNamesrvAddr(this.namesrvAddr);
        //如果需要同一个jvm中不同的producer往不同的mq集群发送消息，需要设置不同的instanceName
        //producer.setInstanceName(instanceName);
        if(this.maxMessageSize!=null){
            producer.setMaxMessageSize(this.maxMessageSize);
        }
        if(this.sendMsgTimeout!=null){
            producer.setSendMsgTimeout(this.sendMsgTimeout);
        }
        if(this.retryTimesWhenSendFailed!=null){
            producer.setRetryTimesWhenSendFailed(this.retryTimesWhenSendFailed);
        }
        try {
            if(isOnOff) {
                producer.start();
            }
        } catch (MQClientException e) {
            e.printStackTrace();
        }
        return producer;
    }
}
