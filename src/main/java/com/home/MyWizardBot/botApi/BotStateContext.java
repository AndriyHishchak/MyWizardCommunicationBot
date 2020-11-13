package com.home.MyWizardBot.botApi;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Component
public class BotStateContext{

    private Map<BotState,InputMessageHandler> messageHandlers = new HashMap<>();

    public BotStateContext(List<InputMessageHandler> messageHandlers) {
        messageHandlers.forEach(handler -> this.messageHandlers.put(handler.getHandlerName(),handler));
    }

    public SendMessage processInputMessage(BotState currentState, Message message) {
        InputMessageHandler currentMessageHandler = findMessageHandler(currentState);
        return currentMessageHandler.handle(message);
    }

    private InputMessageHandler findMessageHandler(BotState carrentState) {
        if (isFillingProfileState(carrentState)) {
            return messageHandlers.get(BotState.FILLING_PROFILE);
        }
        return messageHandlers.get(carrentState);
    }

    private boolean isFillingProfileState(BotState carrentState) {
        switch (carrentState) {
            case ASK_CITY:
            case ASK_DATE:
            case FILLING_PROFILE:
            case PROFILE_FILLED:
                return true;
            default:
                return false;
        }
    }
}
