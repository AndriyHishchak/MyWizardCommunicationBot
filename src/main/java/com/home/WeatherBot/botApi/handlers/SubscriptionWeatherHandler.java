package com.home.WeatherBot.botApi.handlers;

import com.home.WeatherBot.Repo.UserRepo;
import com.home.WeatherBot.Service.ReplyMessagesService;
import com.home.WeatherBot.Service.WeatherService;
import com.home.WeatherBot.botApi.BotState;
import com.home.WeatherBot.botApi.InputMessageHandler;
import com.home.WeatherBot.botApi.playGroup.PlayTimerService;
import com.home.WeatherBot.cache.BotDataCache;
import com.home.WeatherBot.model.User;
import com.home.WeatherBot.model.Weather;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.io.IOException;
import java.net.MalformedURLException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Component
public class SubscriptionWeatherHandler implements InputMessageHandler {

    private static final Logger LOG =  LoggerFactory.getLogger(SubscriptionWeatherHandler.class);

    private final BotDataCache botDataCache;
    private final ReplyMessagesService messagesService;
    private final WeatherService weatherService;

    private final UserRepo userRepo;

    private final Set<User> allUsersSubscription = new HashSet<>();




    public SubscriptionWeatherHandler(BotDataCache botDataCache, ReplyMessagesService messagesService, WeatherService weatherService, UserRepo userRepo) {
        this.botDataCache = botDataCache;
        this.messagesService = messagesService;
        this.weatherService = weatherService;
        this.userRepo = userRepo;


    }
    @Override
    public SendMessage handle(Message message) throws MalformedURLException {
        if (botDataCache.getUsersCurrentBotState(message.getFrom().getId()).equals(BotState.ADD_SUBSCRIPTION)){
            botDataCache.setUsersCurrentBotState(message.getFrom().getId(),BotState.ASK_TIME_BY_SUBSCRIPTION);
        }
        return processUserInput(message);
    }


    @Override
    public BotState getHandlerName() {
        return BotState.ADD_SUBSCRIPTION;
    }



    private SendMessage processUserInput(Message inputMessage) throws MalformedURLException {

        String usersAnswer = inputMessage.getText();
        long userId = inputMessage.getFrom().getId();
        long chatId = inputMessage.getChatId();

        createUser(userId);

        BotState botState = botDataCache.getUsersCurrentBotState(userId);

        SendMessage replyToUser = null;

        if(botState.equals(BotState.ASK_TIME_BY_SUBSCRIPTION)){

            replyToUser = messagesService.getReplyMessage(chatId,"reply.askTime");
            botDataCache.setUsersCurrentBotState(userId, BotState.GET_SUBSCRIPTION_BY_TIME);

        }else if(botState.equals(BotState.GET_SUBSCRIPTION_BY_TIME)){

            User user = userRepo.findByIdUser(userId);
            usersAnswer+=":00";
            user.setTimeOfNotification(LocalTime.parse(usersAnswer));
            userRepo.save(user);

            botDataCache.setUsersCurrentBotState(userId, BotState.ASK_CITY_BY_SUBSCRIPTION);
            replyToUser = messagesService.getReplyMessage(chatId,"reply.askCity");

        }else if(botState.equals(BotState.ASK_CITY_BY_SUBSCRIPTION)) {
            weatherService.getCityWather(usersAnswer);
            User user = userRepo.findByIdUser(userId);
            user.setCity(usersAnswer);
            user.setDateUpdate(new GregorianCalendar());
            user.setSubscription(true);
            userRepo.save(user);


            allUsersSubscription.add(user);

            replyToUser = new SendMessage(chatId, "Ви успішно підключили підписку☑.\n" +
                    "Тепер ви зможете получати погоду о (" + user.getTimeOfNotification() + ") " +
                    "Вам буде приходити повідомлення про погоду кожного дня \uD83C\uDF24");

            /*
            TODO: quartz
            TODO: timerService.runJob(user);
            */

            replyToUser.setReplyMarkup(getInlineMessageButtons());
            botDataCache.setUsersCurrentBotState(userId, BotState.WELCOME_MENU);
        }else if (botState.equals(BotState.DELETE_SUBSCRIPTION)) {
            User user = userRepo.findByIdUser(userId);
            user.setSubscription(false);
            userRepo.save(user);
            allUsersSubscription.remove(user);
            replyToUser = new SendMessage(chatId, "Ви успішно скасували підписку ☑.");
            replyToUser.setReplyMarkup(getInlineMessageButtons());
            LOG.info("User [ " + user.getIdUser() + " ] скавував підписку");
            botDataCache.setUsersCurrentBotState(userId, BotState.WELCOME_MENU);
        }
        return replyToUser;
    }


    @Scheduled(cron = "0 * * * * *")
    public void runJobSend() throws IOException {

        for (User value : allUsersSubscription) {
            if (value.getTimeOfNotification().getHour() == LocalDateTime.now().getHour() &&
                    value.getTimeOfNotification().getMinute() == LocalDateTime.now().getMinute()) {

                LOG.info("User [ " + value.getIdUser() + " ] Получив погоду");
                LOG.info("В даний момент година [ " + LocalDateTime.now().getHour() + ":" + LocalDateTime.now().getMinute() + " ] а user - [ " + value.getTimeOfNotification().getHour() + ":" + value.getTimeOfNotification().getMinute() + " ]");
                User user = userRepo.findByIdUser(value.getIdUser());
                String url = "https://api.telegram.org/" +
                        "bot1460285168:AAFxno35vzbc_uiD2HFp4Ys0vK9dSBSfeaE/sendMessage?" +
                        "chat_id=" + user.getIdUser() +
                        "&text=" + (weatherService.getWeather(value.getCity(), new Weather()));

                RestTemplate restTemplate = new RestTemplate();
                ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            } else {
                LOG.info("User [ " + value.getIdUser() + " ] Не получив погоду");
                LOG.info("В даний момент година [ " + LocalDateTime.now().getHour() + ":" + LocalDateTime.now().getMinute() + " ] а user - [ " + value.getTimeOfNotification().getHour() + ":" + value.getTimeOfNotification().getMinute() + " ]");
            }
        }
    }
    private void createUser (long userId) {
        if (!userRepo.existsByIdUser(userId)) {
            User user = new User(userId,new GregorianCalendar());
            userRepo.save(user);
        }
    }

    private InlineKeyboardMarkup getInlineMessageButtons() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton backMenu = new InlineKeyboardButton().setText("Повернутися в головне меню \uD83D\uDD79");
        backMenu.setCallbackData("backMenu");

        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        keyboardButtonsRow1.add(backMenu);

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);

        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }
}
