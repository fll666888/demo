package com.example.demo.controller;

import com.example.demo.quartz.QuartzManager;
import io.swagger.annotations.Api;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;

@RestController
@RequestMapping("/quartz")
@Api(value = "quartz", tags = "quartz管理")
public class ModifyCronController {

    @Autowired
    private QuartzManager quartzManager;

    @GetMapping("modify")
    public String modify(String cron) throws SchedulerException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        quartzManager.pauseJob(QuartzManager.JOB1,QuartzManager.GROUP1);
        System.out.println(sdf.format(System.currentTimeMillis()) + "：已暂停任务");
        quartzManager.modifyJob(QuartzManager.JOB1,QuartzManager.GROUP1,cron);
        System.out.println(sdf.format(System.currentTimeMillis()) + "：已修改执行计划周期");
        return "ok";
    }

}
