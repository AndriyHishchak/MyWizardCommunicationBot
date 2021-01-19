package com.home.WeatherBot.botApi.jobs;


import com.home.WeatherBot.botApi.handlers.SubscriptionWeatherHandler;
import lombok.SneakyThrows;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

@Controller
public class Jobs implements Job {

    private SubscriptionWeatherHandler subscriptionWeatherHandler;
    private static final Logger LOG =  LoggerFactory.getLogger(Jobs.class);

    public Jobs( SubscriptionWeatherHandler subscriptionWeatherHandler) {
        this.subscriptionWeatherHandler = subscriptionWeatherHandler;
    }

    @SneakyThrows
    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        subscriptionWeatherHandler.runJobSend();
    }
}
