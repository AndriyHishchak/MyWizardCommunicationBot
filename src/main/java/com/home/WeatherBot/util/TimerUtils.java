package com.home.WeatherBot.util;


import com.home.WeatherBot.model.User;
import org.quartz.*;

public class TimerUtils  {
        private TimerUtils() {}

        public static JobDetail buildJobDetail( Class jobClass,  User user) {

            final JobDataMap jobDataMap = new JobDataMap();

            jobDataMap.put(user.getIdUser()+"", user);

            return JobBuilder
                    .newJob(jobClass) 
                   .withIdentity(user.getIdUser()+"")
                    .setJobData(jobDataMap)
                    .build();
        }

        public static Trigger buildTrigger(  User user) {

            return TriggerBuilder
                    .newTrigger()
                    .withIdentity(user.getIdUser()+"")
                    .withSchedule(CronScheduleBuilder.dailyAtHourAndMinute
                            (user.getTimeOfNotification().getHour(), user.getTimeOfNotification().getMinute()))
                    .build();
        }

}
