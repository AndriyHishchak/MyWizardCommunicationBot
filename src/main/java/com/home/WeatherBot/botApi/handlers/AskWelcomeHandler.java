package com.home.WeatherBot.botApi.handlers;

import com.home.WeatherBot.Service.ReplyMessagesService;
import com.home.WeatherBot.botApi.BotState;
import com.home.WeatherBot.botApi.InputMessageHandler;
import com.home.WeatherBot.cache.BotDataCache;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
@Component
public class AskWelcomeHandler implements InputMessageHandler {

    private final BotDataCache botDataCache;
    private final ReplyMessagesService messagesSevice;

    public AskWelcomeHandler(BotDataCache botDataCache, ReplyMessagesService messagesSevice) {
        this.botDataCache = botDataCache;
        this.messagesSevice = messagesSevice;
    }

    @Override
    public SendMessage handle(Message message) {
        return processUsersInput(message);
    }

    @Override
    public BotState getHandlerName() {
        return BotState.WELCOME_MENU;
    }

    private SendMessage processUsersInput (Message inputMessage) {
        int userId = inputMessage.getFrom().getId();
        long chatId = inputMessage.getChatId();

        SendMessage replyToUser = messagesSevice.getReplyMessage(chatId,"reply.askWelcome");
        botDataCache.setUsersCurrentBotState(userId,BotState.FILLING_WEATHER_REQUEST);
        return replyToUser;
    }
}
