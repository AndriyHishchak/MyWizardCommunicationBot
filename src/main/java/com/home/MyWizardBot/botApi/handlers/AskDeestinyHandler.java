package com.home.MyWizardBot.botApi.handlers;

import com.home.MyWizardBot.Service.ReplyMessagesSevice;
import com.home.MyWizardBot.botApi.BotState;
import com.home.MyWizardBot.botApi.InputMessageHandler;
import com.home.MyWizardBot.cache.BotDataCache;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
@Component
public class AskDeestinyHandler implements InputMessageHandler {

    private final BotDataCache botDataCache;
    private final ReplyMessagesSevice messagesSevice;

    public AskDeestinyHandler(BotDataCache botDataCache, ReplyMessagesSevice messagesSevice) {
        this.botDataCache = botDataCache;
        this.messagesSevice = messagesSevice;
    }

    @Override
    public SendMessage handle(Message message) {
        return processUsersInput(message);
    }

    @Override
    public BotState getHandlerName() {
        return BotState.ASK_DESTINY;
    }

    private SendMessage processUsersInput (Message inputMessage) {
        int userId = inputMessage.getFrom().getId();
        long chatId = inputMessage.getChatId();

        SendMessage replyToUser = messagesSevice.getReplyMessage(chatId,"reply.askDestiny");
        botDataCache.setUsersCarrentBotState(userId,BotState.FILLING_PROFILE);
        return replyToUser;
    }
}
