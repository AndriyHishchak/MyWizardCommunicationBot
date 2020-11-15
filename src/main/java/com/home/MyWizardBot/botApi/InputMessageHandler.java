package com.home.MyWizardBot.botApi;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.io.IOException;

public interface InputMessageHandler {

    SendMessage handle (Message message) throws IOException;

    BotState getHandlerName();
}