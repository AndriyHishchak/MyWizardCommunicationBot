package com.home.WeatherBot.cache;

import com.home.WeatherBot.botApi.BotState;
import com.home.WeatherBot.botApi.handlers.WeatherData;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class BotDataCache implements DataCache {

   private final Map<Integer,BotState> usersBotStates = new HashMap<>();
   private final Map<Integer, WeatherData> profileForms = new HashMap<>();

    @Override
    public BotState getUsersCurrentBotState(int userId) {
        BotState botState = usersBotStates.get(userId);
        if (botState == null) {
            botState = BotState.WELCOME_MENU;
        }
        return botState;
    }

    @Override
    public WeatherData getWeatherData(int userId) {
        WeatherData weatherData = profileForms.get(userId);
        if (weatherData == null) {
            weatherData = new WeatherData();
        }
        return weatherData;
    }
    @Override
    public void setUsersCurrentBotState(int userId, BotState botState) {
         usersBotStates.put(userId,botState);
    }

    @Override
    public void saveWeatherData(int userId, WeatherData weatherData) {
        profileForms.put(userId, weatherData);
    }
}
