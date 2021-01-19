package com.home.WeatherBot;

import com.home.WeatherBot.botApi.TelegramFacade;
import lombok.Setter;
import lombok.SneakyThrows;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;


@Setter
public class WeatherTelegramBot extends TelegramWebhookBot {

    private String webHookPath;
    private String botName;
    private String botToken;

    private TelegramFacade telegramFacade;

    public WeatherTelegramBot(TelegramFacade telegramFacade) {
        this.telegramFacade = telegramFacade;
    }


    @SneakyThrows
    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        final BotApiMethod<?> replyMessageToUser = telegramFacade.handleUpdate(update);

        return replyMessageToUser;
    }


    @Override
    public String getBotUsername() {
        return botName;
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
