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
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

@Component
public class FillingWeatherHandler implements InputMessageHandler {

    private final BotDataCache botDataCache;
    private final ReplyMessagesService messagesService;
    private final WeatherService weatherService;
    private final Weather weather = new Weather();
    private final UserRepo userRepo;
    private String city;
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
                replyToUser.setReplyMarkup(getInlineMessageButtons());
        }
        return replyToUser;
    }

    private void savingUserData (long userId) {
        User user;
        if (!userRepo.existsByIdUser(userId)) {
            user = new User(userId, city, false, new GregorianCalendar());
            userRepo.save(user);
        }
    }

    private InlineKeyboardMarkup getInlineMessageButtons() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton backMenu = new InlineKeyboardButton().setText("Повернутися в головне меню \uD83D\uDD79");
        InlineKeyboardButton againRain = new InlineKeyboardButton().setText("Переглянути погоду щераз ⛈");
        backMenu.setCallbackData("backMenu");
        againRain.setCallbackData("againRain");

        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        keyboardButtonsRow1.add(backMenu);

        List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<>();
        keyboardButtonsRow2.add(againRain);

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);
        rowList.add(keyboardButtonsRow2);

        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }


}
