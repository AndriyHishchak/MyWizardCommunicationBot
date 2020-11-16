package com.home.WeatherBot.cache;

import com.home.WeatherBot.botApi.BotState;
import com.home.WeatherBot.botApi.handlers.WeatherData;

public interface DataCache {
    BotState getUsersCurrentBotState (int userId);
    void setUsersCurrentBotState (int userId,BotState botState);

    WeatherData getWeatherData(int userId);

    void saveWeatherData(int userId, WeatherData weatherData);
}
