package com.eac.job.task;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.SimpleFormatter;

/**
 * <pre>
 * Title :  TODO
 * </pre>
 *
 * @author : SunJiYong
 * @since : 2022-02-22
 **/
@Component
public class ScheduledTask {

    private static final SimpleDateFormat dataFormat=new SimpleDateFormat("YYYY-MM-DD HH:mm:ss");
//
//    @Scheduled(fixedRate = 50000)
//    public void testFixedRate(){
//        System.out.println("testFixedRatetestFixedRate==>"+dataFormat.format(new Date()));
//
//    }
//
//    @Scheduled(cron = "0 0/1 * * * ?")
//    public void testCron(){
//        System.out.println("testCrontestCrontestCrontest==>"+dataFormat.format(new Date()));
//
//    }
}
