package com.example.demo.mq.rocketmq;

import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 监听器（接收消息，处理消息）
 */
@Component
public class MsgListener implements MessageListenerConcurrently {

    private static final Logger LOG = LoggerFactory.getLogger(MsgListener.class) ;

    @Resource
    private ParamConfigService paramConfigService ;

    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext context) {
        if (list == null || list.isEmpty()){
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        }

        for(MessageExt messageExt : list) {
            LOG.info("接收到的消息为："+new String(messageExt.getBody()));
            //判断该消息是否重复消费（RocketMQ不保证消息不重复，如果你的业务需要保证严格的不重复消息，需要你自己在业务端去重）
            //获取该消息重试次数
            int reConsume = messageExt.getReconsumeTimes();
            //消息已经重试了3次，如果不需要再次消费，则返回成功
            if(reConsume == 3){
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
            if(messageExt.getTopic().equals(paramConfigService.platTopic)){
                String tags = messageExt.getTags() ;
                if(tags.equals(paramConfigService.accountTag)) {
                    LOG.info("匹配到Tag ==>" + tags);
                    //业务处理
                } else {
                    LOG.info("未匹配到Tag ==>" + tags);
                }
            }
        }

        // 消息消费成功（如果没有return success，consumer会重新消费该消息，直到return success）
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }
}
