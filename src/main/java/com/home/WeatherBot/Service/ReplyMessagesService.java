package com.home.WeatherBot.Service;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
@Service 
public class ReplyMessagesService {

    private LocaleMessagesService localeMessagesService;

    public ReplyMessagesService(LocaleMessagesService localeMessagesService) {
        this.localeMessagesService = localeMessagesService;
    }

    public SendMessage getReplyMessage(long chatId, String replyMessage) {
        return new SendMessage(chatId, localeMessagesService.getMessage(replyMessage));
    }



    public String getReplyText(String replyText) {
        return localeMessagesService.getMessage(replyText);
    }
}
