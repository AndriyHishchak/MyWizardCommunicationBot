package com.home.MyWizardBot.botApi.handlers;

import com.home.MyWizardBot.Service.ReplyMessagesSevice;
import com.home.MyWizardBot.botApi.BotState;
import com.home.MyWizardBot.botApi.InputMessageHandler;
import com.home.MyWizardBot.cache.BotDataCache;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
@Component
public class FillingProfileHandler implements InputMessageHandler {

    private final BotDataCache botDataCache;
    private final ReplyMessagesSevice messagesSevice;

    public FillingProfileHandler(BotDataCache botDataCache, ReplyMessagesSevice messagesSevice) {
        this.botDataCache = botDataCache;
        this.messagesSevice = messagesSevice;
    }

    @Override
    public SendMessage handle(Message message) {
       if(botDataCache.getUsersCarrentBotState(message.getFrom().getId()).equals(BotState.FILLING_PROFILE)){
           botDataCache.setUsersCarrentBotState(message.getFrom().getId(),BotState.ASK_CITY);
       }
        return processUserInput(message);
    }

    @Override
    public BotState getHandlerName() {
        return BotState.FILLING_PROFILE;
    }

    private SendMessage processUserInput(Message inputMessage) {
        String usersAnswer = inputMessage.getText();
        int userId = inputMessage.getFrom().getId();
        long chatId = inputMessage.getChatId();

        ProfileFormData profileFormData = botDataCache.getProfileData(userId);

        BotState botState = botDataCache.getUsersCarrentBotState(userId);

        SendMessage replyToUser = null;

        if(botState.equals(BotState.ASK_CITY)) {
            replyToUser = messagesSevice.getReplyMessage(chatId,"reply.askCity");
            botDataCache.setUsersCarrentBotState(userId, BotState.ASK_DATE);
        }
        if(botState.equals(BotState.ASK_DATE)) {
            replyToUser = messagesSevice.getReplyMessage(chatId,"reply.askDate");
            profileFormData.setCity(usersAnswer);
            botDataCache.setUsersCarrentBotState(userId, BotState.PROFILE_FILLED);
        }

        if(botState.equals(BotState.PROFILE_FILLED)) {
            profileFormData.setDate(usersAnswer);
            botDataCache.setUsersCarrentBotState(userId, BotState.FILLING_PROFILE);
            replyToUser = new SendMessage(chatId,String.format("%s %s Дані форми : ",profileFormData));
        }
        botDataCache.saveProfileFormData(userId,profileFormData);
        return replyToUser;
    }
}
