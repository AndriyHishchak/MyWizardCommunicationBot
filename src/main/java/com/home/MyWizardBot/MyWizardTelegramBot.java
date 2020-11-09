package com.home.MyWizardBot;

import lombok.Setter;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;


@Setter
public class MyWizardTelegramBot extends TelegramWebhookBot {

    private String webHookPath;
    private String botUserName;
    private String botToken;

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {

        SendMessage sendMessage = null;
        long chatID = update.getMessage().getChatId();
        String message = update.getMessage().getText();

        if (update.getMessage() != null && update.getMessage().hasText()) {
            sendMessage = new SendMessage(chatID,"Hi" + message);
        }
        if (!update.getMessage().hasText()) {
            sendMessage = new SendMessage(chatID,"I can only recognize the text of messages");
        }
        return sendMessage;
    }

    @Override
    public String getBotUsername() {
        return botUserName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public String getBotPath() {
        return webHookPath;
    }
}
