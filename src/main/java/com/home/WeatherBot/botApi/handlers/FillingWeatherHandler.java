package com.home.WeatherBot.botApi.handlers;

import com.home.WeatherBot.Service.ReplyMessagesService;
import com.home.WeatherBot.Service.WeatherService;
import com.home.WeatherBot.botApi.BotState;
import com.home.WeatherBot.botApi.InputMessageHandler;
import com.home.WeatherBot.cache.BotDataCache;
import com.home.WeatherBot.model.Weather;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.io.IOException;

@Component
public class FillingWeatherHandler implements InputMessageHandler {

    private final BotDataCache botDataCache;
    private final ReplyMessagesService messagesService;
    private final WeatherService weatherService;
    private final Weather weather = new Weather();
    public FillingWeatherHandler(BotDataCache botDataCache, ReplyMessagesService messagesService, WeatherService weatherService
                                 ) {
        this.botDataCache = botDataCache;
        this.messagesService = messagesService;
        this.weatherService = weatherService;

    }

    @Override
    public SendMessage handle(Message message) throws IOException {
       if(botDataCache.getUsersCurrentBotState(message.getFrom().getId()).equals(BotState.FILLING_WEATHER_REQUEST)){
           botDataCache.setUsersCurrentBotState(message.getFrom().getId(),BotState.ASK_CITY);
       }

        return processUserInput(message);
    }

    @Override
    public BotState getHandlerName() {
        return BotState.FILLING_WEATHER_REQUEST;
    }

    private SendMessage processUserInput(Message inputMessage) throws IOException {
        String usersAnswer = inputMessage.getText();
        int userId = inputMessage.getFrom().getId();
        long chatId = inputMessage.getChatId();

        WeatherData weatherData = botDataCache.getWeatherData(userId);

        BotState botState = botDataCache.getUsersCurrentBotState(userId);

        SendMessage replyToUser = null;

        if(botState.equals(BotState.ASK_CITY)) {
            replyToUser = messagesService.getReplyMessage(chatId,"reply.askCity");
            botDataCache.setUsersCurrentBotState(userId, BotState.GET_WEATHER_BY_CITY);
        }else if(botState.equals(BotState.GET_WEATHER_BY_CITY)) {
            weatherData.setCity(usersAnswer);
            botDataCache.setUsersCurrentBotState(userId, BotState.FILLING_WEATHER_REQUEST);
            replyToUser = new SendMessage(chatId, String.valueOf(weatherService.getWeather(weatherData.getCity(),weather)));
        }
        botDataCache.saveWeatherData(userId, weatherData);
        return replyToUser;
    }
}
