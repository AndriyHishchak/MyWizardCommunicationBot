package com.home.WeatherBot.botApi.handlers;

import com.home.WeatherBot.Repo.UserRepo;
import com.home.WeatherBot.Service.ReplyMessagesService;
import com.home.WeatherBot.Service.WeatherService;
import com.home.WeatherBot.botApi.BotState;
import com.home.WeatherBot.botApi.InputMessageHandler;
import com.home.WeatherBot.cache.BotDataCache;
import com.home.WeatherBot.model.User;
import com.home.WeatherBot.model.Weather;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.io.IOException;
import java.util.GregorianCalendar;

@Component
public class FillingWeatherHandler implements InputMessageHandler {

    private final BotDataCache botDataCache;
    private final ReplyMessagesService messagesService;
    private final WeatherService weatherService;
    private final Weather weather = new Weather();
    private final UserRepo userRepo;
    String city;
    public FillingWeatherHandler(BotDataCache botDataCache, ReplyMessagesService messagesService, WeatherService weatherService,
                                 UserRepo userRepo) {
        this.botDataCache = botDataCache;
        this.messagesService = messagesService;
        this.weatherService = weatherService;
        this.userRepo = userRepo;
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
        long userId = inputMessage.getFrom().getId();
        long chatId = inputMessage.getChatId();

        BotState botState = botDataCache.getUsersCurrentBotState(userId);

        SendMessage replyToUser = null;


        if(botState.equals(BotState.ASK_CITY)) {
           replyToUser = messagesService.getReplyMessage(chatId,"reply.askCity");
            botDataCache.setUsersCurrentBotState(userId, BotState.GET_WEATHER_BY_CITY);
        }else if(botState.equals(BotState.GET_WEATHER_BY_CITY)) {
            city = usersAnswer;
            botDataCache.setUsersCurrentBotState(userId, BotState.FILLING_WEATHER_REQUEST);
                savingUserData(userId);
                replyToUser = new SendMessage(chatId, String.valueOf(weatherService.getWeather(city,weather)));

        }
        return replyToUser;
    }

    private void savingUserData (long userId) {
        User user;
        if (!userRepo.existsByIdUser(userId)) {
            user = new User(userId, null , null, false, new GregorianCalendar());
            userRepo.save(user);
        }

    }


}
