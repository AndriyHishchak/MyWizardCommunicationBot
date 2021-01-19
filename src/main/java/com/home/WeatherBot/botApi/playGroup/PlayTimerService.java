package com.home.WeatherBot.botApi.playGroup;


import com.home.WeatherBot.Service.SchedulerService;
import com.home.WeatherBot.botApi.jobs.Jobs;
import com.home.WeatherBot.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlayTimerService {

    private final SchedulerService schedulerService;

    @Autowired
    public PlayTimerService(SchedulerService schedulerService) {
        this.schedulerService = schedulerService;
    }


    public void runJob(User user){
        schedulerService.schedule(Jobs.class, user);
    }



}
