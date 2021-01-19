package com.home.WeatherBot.cache;

import com.home.WeatherBot.botApi.BotState;
import com.home.WeatherBot.botApi.jobs.Jobs;
import com.home.WeatherBot.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class BotDataCache implements DataCache {

   private final Map<Long,BotState> usersBotStates = new HashMap<>();
   private static final Logger LOG =  LoggerFactory.getLogger(BotDataCache.class);

    @Override
    public BotState getUsersCurrentBotState(long userId) {
        BotState botState = usersBotStates.get(userId);
        if (botState == null) {
            botState = BotState.WELCOME_MENU;
        }
        return botState;
    }


    @Override
    public void setUsersCurrentBotState(long userId, BotState botState) {
         usersBotStates.put(userId,botState);
         LOG.info("Бот User ["+userId+"] в состояниє - ["+botState+"] [ " + new Date() + " ]");

    }


}
