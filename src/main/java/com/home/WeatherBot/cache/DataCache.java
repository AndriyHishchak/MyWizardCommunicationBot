package com.home.WeatherBot.cache;

import com.home.WeatherBot.botApi.BotState;
import com.home.WeatherBot.model.User;

public interface DataCache {
    BotState getUsersCurrentBotState (long userId);
    void setUsersCurrentBotState (long userId,BotState botState);

    User getUserData(long userId);

}
