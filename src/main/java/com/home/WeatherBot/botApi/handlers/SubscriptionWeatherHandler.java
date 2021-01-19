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
import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.Date;
import java.util.GregorianCalendar;

@Component
public class SubscriptionWeatherHandler implements InputMessageHandler {

    private final BotDataCache botDataCache;
    private final ReplyMessagesService messagesService;
    private final WeatherService weatherService;
    private final Weather weather = new Weather();
    private final UserRepo userRepo;


  /*  static final DateTimeFormatter FMT = new DateTimeFormatterBuilder()
            .appendPattern("HH:mm:ss")
            //.parseDefaulting(ChronoField.NANO_OF_DAY, 0)
            .toFormatter()
            .withZone(ZoneId.of("Europe/Kiev"));*/

    LocalTime timeOfNotification;
    String city;
    boolean isSubscription;

    public SubscriptionWeatherHandler(BotDataCache botDataCache, ReplyMessagesService messagesService, WeatherService weatherService, UserRepo userRepo) {
        this.botDataCache = botDataCache;
        this.messagesService = messagesService;
        this.weatherService = weatherService;
        this.userRepo = userRepo;
    }
    @Override
    public SendMessage handle(Message message) throws IOException {
        if (botDataCache.getUsersCurrentBotState(message.getFrom().getId()).equals(BotState.ADD_SUBSCRIPTION)){
            botDataCache.setUsersCurrentBotState(message.getFrom().getId(),BotState.ASK_TIME_BY_SUBSCRIPTION);
        }
        return processUserInput(message);
    }


    @Override
    public BotState getHandlerName() {
        return BotState.ADD_SUBSCRIPTION;
    }

    private SendMessage processUserInput(Message inputMessage) throws IOException {
        String usersAnswer = inputMessage.getText();
        long userId = inputMessage.getFrom().getId();
        long chatId = inputMessage.getChatId();

        BotState botState = botDataCache.getUsersCurrentBotState(userId);

        SendMessage replyToUser = null;

        if(botState.equals(BotState.ASK_TIME_BY_SUBSCRIPTION)){
            replyToUser = messagesService.getReplyMessage(chatId,"reply.askTime");
            botDataCache.setUsersCurrentBotState(userId, BotState.GET_SUBSCRIPTION_BY_TIME);
        }else if(botState.equals(BotState.GET_SUBSCRIPTION_BY_TIME)){
            usersAnswer+=":00";
            timeOfNotification = LocalTime.parse(usersAnswer);

            isSubscription = true;
            botDataCache.setUsersCurrentBotState(userId, BotState.ASK_CITY_BY_SUBSCRIPTION);
            replyToUser = messagesService.getReplyMessage(chatId,"reply.askCity");
        }else if(botState.equals(BotState.ASK_CITY_BY_SUBSCRIPTION)) {
            city = usersAnswer;
            savingDataForSubscription(userId);

            replyToUser = new SendMessage(chatId,"Ви успешно подключили подписку☑.\n" +
                    "C завтрошнього дня и в последущиє дни о ("+timeOfNotification+") " +
                    "вам будем приходить уведомлениє о погоде\uD83C\uDF24");

            botDataCache.setUsersCurrentBotState(userId, BotState.GET_SUBSCRIPTION_BY_CITY);
        }else if(botState.equals(BotState.GET_SUBSCRIPTION_BY_CITY)) {
            botDataCache.setUsersCurrentBotState(userId, BotState.ADD_SUBSCRIPTION);}

        return replyToUser;
    }


    private void savingDataForSubscription (long userId) {
        User user;
        if (!userRepo.existsByIdUser(userId)) {
            user = new User(userId, city, timeOfNotification, isSubscription,new GregorianCalendar());
        }else {
            user = userRepo.findByIdUser(userId);
            user.setCity(city);
            user.setTimeOfNotification(timeOfNotification);
            user.setSubscription(isSubscription);
            user.setDateUpdate(new GregorianCalendar());
        }
        userRepo.save(user);
    }


}
