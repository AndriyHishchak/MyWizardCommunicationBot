package com.home.WeatherBot.botApi;

import com.home.WeatherBot.cache.BotDataCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.IOException;

@Component
@Slf4j
public class TelegramFacade {

    private final BotStateContext botStateContext;
    private final BotDataCache botDataCache;

    public TelegramFacade(BotStateContext botStateContext, BotDataCache botDataCache) {
        this.botStateContext = botStateContext;
        this.botDataCache = botDataCache;
    }

    public SendMessage handleUpdate(Update update) throws IOException {

        SendMessage replyMessage = null;

        Message message = update.getMessage();
        if(message !=null && message.hasText()) {
           log.info("New message from User: {}, chatId: {}, with text: {}",
                   message.getFrom().getUserName(),
                   message.getChatId(),
                   message.getText());
           replyMessage = handleInputMessage(message);
        }
        return replyMessage;
    }

    private SendMessage handleInputMessage(Message message) throws IOException {
        String inputMessage = message.getText();
        int userId = message.getFrom().getId();
        BotState botState;
        SendMessage replyMessage;

        switch (inputMessage) {
            case "/start":
                botState = BotState.WELCOME_MENU;
                break;
            case "/show_weather":
                botState = BotState.FILLING_WEATHER_REQUEST;
                break;
            case "/Help":
                botState = BotState.SHOW_HELP_MENU;
                break;
            default:
                botState = botDataCache.getUsersCurrentBotState(userId);
                break;
        }
        botDataCache.setUsersCurrentBotState(userId,botState);
        replyMessage = botStateContext.processInputMessage(botState,message);
        return replyMessage;
    }

}
