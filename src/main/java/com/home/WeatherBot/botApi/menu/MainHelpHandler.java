package com.home.WeatherBot.botApi.menu;

import com.home.WeatherBot.Service.MainMenuService;
import com.home.WeatherBot.Service.ReplyMessagesService;
import com.home.WeatherBot.botApi.BotState;
import com.home.WeatherBot.botApi.InputMessageHandler;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.io.IOException;

@Component
public class MainHelpHandler implements InputMessageHandler {

    private ReplyMessagesService messagesService;
    private MainMenuService mainMenuService;

    public MainHelpHandler(ReplyMessagesService messagesService, MainMenuService mainMenuService) {
        this.messagesService = messagesService;
        this.mainMenuService = mainMenuService;
    }

    @Override
    public SendMessage handle(Message message) throws IOException {
        return mainMenuService.getMainMenuMessage(message.getChatId(),
                messagesService.getReplyText("reply.showHelpMenu"));
    }

    @Override
    public BotState getHandlerName() {
        return BotState.SHOW_HELP_MENU;
    }
}
