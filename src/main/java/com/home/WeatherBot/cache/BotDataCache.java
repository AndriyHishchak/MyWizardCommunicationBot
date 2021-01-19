package com.home.WeatherBot.cache;

import com.home.WeatherBot.botApi.BotState;
import com.home.WeatherBot.model.User;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class BotDataCache implements DataCache {

   private final Map<Long,BotState> usersBotStates = new HashMap<>();
   private final Map<Long, User> profileForms = new HashMap<>();

    @Override
    public BotState getUsersCurrentBotState(long userId) {
        BotState botState = usersBotStates.get(userId);
        if (botState == null) {
            botState = BotState.WELCOME_MENU;
        }
        return botState;
    }

    @Override
    public User getUserData(long userId) {
        User user = profileForms.get(userId);
        if (user == null) {
            user = new User();
        }
        return user;
    }
    @Override
    public void setUsersCurrentBotState(long userId, BotState botState) {
         usersBotStates.put(userId,botState);
         System.out.println("Бот User ["+userId+"] в состояниє - ["+botState+"]");

    }


}
