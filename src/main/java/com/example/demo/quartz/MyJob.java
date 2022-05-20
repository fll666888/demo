package com.example.demo.quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;

import java.text.SimpleDateFormat;

public class MyJob implements Job {

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        System.out.println(sdf.format(System.currentTimeMillis()) + "：任务开始执行了");
        try {
            executeTask();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        System.out.println(sdf.format(System.currentTimeMillis()) + "：任务执行结束了");
    }

    private void executeTask() throws SchedulerException {

    }


}
