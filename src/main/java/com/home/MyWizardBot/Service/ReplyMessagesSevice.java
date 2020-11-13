package com.home.MyWizardBot.Service;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
@Service 
public class ReplyMessagesSevice {

    private LocaleMessagesService localeMessagesService;

    public ReplyMessagesSevice(LocaleMessagesService localeMessagesService) {
        this.localeMessagesService = localeMessagesService;
    }

    public SendMessage getReplyMessage(long chatId, String replyMessage ) {
        return new SendMessage(chatId,localeMessagesService.getMessage(replyMessage));
    }

}
