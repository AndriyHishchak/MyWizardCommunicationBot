package com.home.MyWizardBot.botApi;

import com.home.MyWizardBot.cache.BotDataCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@Slf4j
public class TelegramFacade {

    private final BotStateContext botStateContext;
    private final BotDataCache botDataCache;

    public TelegramFacade(BotStateContext botStateContext, BotDataCache botDataCache) {
        this.botStateContext = botStateContext;
        this.botDataCache = botDataCache;
    }

    public SendMessage handleUpdate(Update update) {
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

    private SendMessage handleInputMessage(Message message) {
        String inputMessage = message.getText();
        int userId = message.getFrom().getId();
        BotState botState;
        SendMessage replyMessage;

        switch (inputMessage) {
            case "/start":
                botState = BotState.ASK_DESTINY;
                break;
            case "Получити прогноз погоди":
                botState = BotState.FILLING_PROFILE;
                break;
            case "Допомога":
                botState = BotState.SHOW_HELP_MENU;
                break;
            default:
                botState = botDataCache.getUsersCarrentBotState(userId);
                break;
        }
        botDataCache.setUsersCarrentBotState(userId,botState);
        replyMessage = botStateContext.processInputMessage(botState,message);
        return replyMessage;
    }

}
