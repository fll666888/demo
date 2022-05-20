package com.example.demo;

import org.apache.rocketmq.client.log.ClientLogger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
//@ServletComponentScan //自动配置druid：添加servlet组件扫描，使得Spring能够扫描到我们编写的druid包下servlet（DruidStatViewServlet）和filter（DruidStatFilter）
public class DemoApplication {

    public static void main(String[] args) {
        //rocketmq日志配置
        System.setProperty(ClientLogger.CLIENT_LOG_USESLF4J, "true");
        SpringApplication.run(DemoApplication.class, args);
    }

}
