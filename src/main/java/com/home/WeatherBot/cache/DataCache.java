package com.home.WeatherBot.cache;

import com.home.WeatherBot.botApi.BotState;


public interface DataCache {
    BotState getUsersCurrentBotState (long userId);
    void setUsersCurrentBotState (long userId,BotState botState);



}
