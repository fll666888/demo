package com.example.demo.controller;

import com.example.demo.mq.rocketmq.ParamConfigService;
import com.example.demo.vo.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.*;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageBatch;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.message.MessageQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@RestController
@RequestMapping("/rocketmq")
@Api(value = "rocketmq", tags = "rocketmq管理")
public class RocketmqController {

    @Autowired
    private DefaultMQProducer defaultMQProducer;

    @Autowired
    private ParamConfigService paramConfigService;

    @ApiOperation(value = "同步发送")
    @RequestMapping(value = "/synSendMessage", method = {RequestMethod.GET})
    public SendResult synSendMessage() {
        SendResult sendResult = null;
        String msgInfo = "rocketmq send 同步message";
        try {
            Message sendMsg = new Message(paramConfigService.platTopic,
                    paramConfigService.accountTag, msgInfo.getBytes());
            sendResult = defaultMQProducer.send(sendMsg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sendResult;
    }

    @ApiOperation(value = "异步发送")
    @RequestMapping(value = "/asynSendMessage", method = {RequestMethod.GET})
    public void aSynSendMessage() {
        String msgInfo = "rocketmq send 异步message";
        try {
            Message sendMsg = new Message(paramConfigService.platTopic,
                    paramConfigService.accountTag, msgInfo.getBytes());
            defaultMQProducer.send(sendMsg, new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    System.out.println("发送成功！");
                }

                @Override
                public void onException(Throwable throwable) {
                    System.out.println("发送失败！");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ApiOperation(value = "单向发送")
    @RequestMapping(value = "/oneWaySendMessage", method = {RequestMethod.GET})
    public void oneWaySendMessage() {
        String msgInfo = "rocketmq send 单向message";
        try {
            Message sendMsg = new Message(paramConfigService.platTopic,
                    paramConfigService.accountTag, msgInfo.getBytes());
            defaultMQProducer.sendOneway(sendMsg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ApiOperation(value = "顺序发送")
    @RequestMapping(value = "/orderSendMessage", method = {RequestMethod.GET})
    public void orderSendMessage() {
        String msgInfo = "rocketmq send 顺序message";
        try {
            for (int i = 1; i < 7; i++) {
                Message sendMsg = new Message(paramConfigService.platTopic,
                        paramConfigService.accountTag, (msgInfo + i).getBytes());
                SendResult sendResult = defaultMQProducer.send(
                        // 要发的那条消息
                        sendMsg,
                        // queue 选择器 ，向 topic中的哪个queue去写消息
                        new MessageQueueSelector() {
                            // 手动 选择一个queue
                            @Override
                            public MessageQueue select(
                                    // 当前topic 里面包含的所有queue
                                    List<MessageQueue> mqs,
                                    // 具体要发的那条消息
                                    Message msg,
                                    // 对应到 send（） 里的 args，也就是2000前面的那个0
                                    // 实际业务中可以把0换成实际业务系统的主键，比如订单号啥的，然后这里做hash进行选择queue等。
                                    // 能做的事情很多，我这里做演示就用第一个queue，所以不用arg。
                                    Object arg) {
                                // 向固定的一个queue里写消息，比如这里就是向第一个queue里写消息
                                MessageQueue queue = mqs.get(0);
                                // 选好的queue
                                return queue;
                            }
                        },
                        // 自定义参数：0
                        // 2000代表2000毫秒超时时间
                        0, 2000);
                System.out.println(sendResult);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ApiOperation(value = "批量发送")
    @RequestMapping(value = "/batchSendMessage", method = {RequestMethod.GET})
    public SendResult batchSendMessage() {
        SendResult sendResult = null;
        String msgInfo = "rocketmq send 批量message";
        try {
            Message sendMsg1 = new Message(paramConfigService.platTopic,
                    paramConfigService.accountTag, (msgInfo + 1).getBytes());
            Message sendMsg2 = new Message(paramConfigService.platTopic,
                    paramConfigService.accountTag, (msgInfo + 2).getBytes());
            Message sendMsg3 = new Message(paramConfigService.platTopic,
                    paramConfigService.accountTag, (msgInfo + 3).getBytes());
            List<Message> sendMsgs = new ArrayList<>();

            sendMsgs.add(sendMsg1);
            sendMsgs.add(sendMsg2);
            sendMsgs.add(sendMsg3);
            sendResult = defaultMQProducer.send(sendMsgs);
            return sendResult;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @ApiOperation(value = "定时/延迟发送")
    @RequestMapping(value = "/delaySendMessage", method = {RequestMethod.GET})
    public SendResult delaySendMessage() {
        SendResult sendResult = null;
        String msgInfo = "rocketmq send 定时/延迟message";
        try {
            Message sendMsg = new Message(paramConfigService.platTopic,
                    paramConfigService.accountTag, msgInfo.getBytes());
            //设置延迟队列的level，5表示延迟一分钟
            sendMsg.setDelayTimeLevel(5);
            sendResult = defaultMQProducer.send(sendMsg);
            return sendResult;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @ApiOperation(value = "事务发送")
    @RequestMapping(value = "/transactionSendMessage", method = {RequestMethod.GET})
    public void transactionSendMessage() {
        TransactionMQProducer producer = new TransactionMQProducer("unique_producer_group_name");
        producer.setNamesrvAddr("127.0.0.1:9876");
        ExecutorService executorService = new ThreadPoolExecutor(2, 5, 100, TimeUnit.SECONDS, new ArrayBlockingQueue<>(2000), new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setName("client-transaction-msg-check-thread");
                return thread;
            }
        });
        producer.setExecutorService(executorService);
        producer.setTransactionListener(new TransactionListener() {
            /**
             * 本地事务方法
             * @param message
             * @param arg
             * @return
             */
            @Override
            public LocalTransactionState executeLocalTransaction(Message message, Object arg) {
                LocalTransactionState state;
                //msg-1返回COMMIT_MESSAGE
                if(message.getKeys().equals("msg-1")) {
                    state = LocalTransactionState.COMMIT_MESSAGE;
                }
                //msg-2返回ROLLBACK_MESSAGE
                else if(message.getKeys().equals("msg-2")) {
                    state = LocalTransactionState.ROLLBACK_MESSAGE;
                } else {
                    //这里返回unknown的目的是模拟执行本地事务突然宕机的情况（或者本地执行成功发送确认消息失败的场景）
                    state = LocalTransactionState.UNKNOW;
                }
                System.out.println(message.getKeys() + ",state:" + state);
                return state;
            }

            /**
             * 定时回查方法
             * @param messageExt
             * @return
             */
            @Override
            public LocalTransactionState checkLocalTransaction(MessageExt messageExt) {
                if (null != messageExt.getKeys()) {
                    switch (messageExt.getKeys()) {
                        case "msg-3":
                            System.out.println("msg-3 UNKNOW");
                            return LocalTransactionState.UNKNOW;
                        case "msg-4":
                            System.out.println("msg-4 COMMIT_MESSAGE");
                            return LocalTransactionState.COMMIT_MESSAGE;
                        case "msg-5":
                            //查询到本地事务执行失败，需要回滚消息。
                            System.out.println("msg-5 ROLLBACK_MESSAGE");
                            return LocalTransactionState.ROLLBACK_MESSAGE;
                    }
                }
                return LocalTransactionState.COMMIT_MESSAGE;
            }
        });

        try {
            producer.start();
        } catch (MQClientException e) {
            e.printStackTrace();
        }

        //模拟发送5条消息
        for (int i = 1; i < 6; i++) {
            try {
                Message msg = new Message(paramConfigService.platTopic,
                        paramConfigService.accountTag, "msg-" + i, ("测试，这是事务消息！ " + i).getBytes());
                SendResult sendResult = producer.sendMessageInTransaction(msg, null);
                System.out.println(sendResult);
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //producer.shutdown();

    }

}
