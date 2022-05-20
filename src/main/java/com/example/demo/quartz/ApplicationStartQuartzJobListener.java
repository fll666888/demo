package com.example.demo.quartz;


import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

@Configuration
public class ApplicationStartQuartzJobListener implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private QuartzManager quartzManager;

    @Autowired
    private JobFactory jobFactory;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        try {
            quartzManager.startJob();
            System.out.println("任务已经启动......");
            quartzManager.pauseAllJob();
            System.out.println("任务已经暂停......");
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean() {
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        schedulerFactoryBean.setJobFactory(jobFactory);
        return schedulerFactoryBean;
    }

    //初始化注入scheduler
    @Bean
    public Scheduler scheduler() {
        return schedulerFactoryBean().getScheduler();
    }

}
