package com.example.demo.mq.rocketmq;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ParamConfigService {

    @Value("${plat.plat-topic}")
    public String platTopic;
    @Value("${plat.plat-tag}")
    public String accountTag;
    //省略 get  set
}
