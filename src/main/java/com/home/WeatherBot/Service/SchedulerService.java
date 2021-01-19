package com.home.WeatherBot.Service;

import com.home.WeatherBot.model.User;
import com.home.WeatherBot.util.TimerUtils;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Service
public class SchedulerService {
    private static final Logger LOG =  LoggerFactory.getLogger(SchedulerService.class);
    private final Scheduler scheduler;

    @Autowired
    public SchedulerService( Scheduler scheduler) {
        this.scheduler = scheduler;
    }
    public void schedule( Class jobClass, User user) {

        final JobDetail jobDetail = TimerUtils.buildJobDetail(jobClass,user);
        final Trigger trigger = TimerUtils.buildTrigger(user);

        try{
            if (scheduler.checkExists(jobDetail.getKey())){
                scheduler.deleteJob(jobDetail.getKey());
            }
                scheduler.scheduleJob(jobDetail, trigger);
        }catch (SchedulerException e) {
            LOG.error(e.getMessage(),e);
        }
    }


    @PostConstruct
    public void init() {
        try{
            scheduler.start();
        }catch (SchedulerException e) {
            LOG.error(e.getMessage(), e);
        }
    }

    @PreDestroy
    public void preDestroy() {
        try{
            scheduler.shutdown();
        }catch (SchedulerException e){
            LOG.error(e.getMessage(),e);
        }
    }
}
