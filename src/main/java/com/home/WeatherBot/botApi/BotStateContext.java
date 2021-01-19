package com.home.WeatherBot.botApi;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Component
public class BotStateContext{

    private final Map<BotState,InputMessageHandler> messageHandlers = new HashMap<>();

    public BotStateContext(List<InputMessageHandler> messageHandlers) {
        messageHandlers.forEach(handler -> this.messageHandlers.put(handler.getHandlerName(),handler));
    }

    public SendMessage processInputMessage(BotState currentState, Message message) throws IOException {

        InputMessageHandler currentMessageHandler = findMessageHandler(currentState);
        return currentMessageHandler.handle(message);
    }

    private InputMessageHandler findMessageHandler(BotState currentState) {
        if (isFillingProfileState(currentState)) {
            return messageHandlers.get(BotState.FILLING_WEATHER_REQUEST);
        }else if (isFillingSubscriptionState(currentState)) {
            return messageHandlers.get(BotState.ADD_SUBSCRIPTION);
        }
        return messageHandlers.get(currentState);
    }

    private boolean isFillingProfileState(BotState currentState) {
        switch (currentState) {
            case ASK_CITY:
            case FILLING_WEATHER_REQUEST:
            case GET_WEATHER_BY_CITY:
                return true;
            default:
                return false;
        }
    }
    private boolean isFillingSubscriptionState(BotState currentState) {
        switch (currentState) {
            case ASK_CITY_BY_SUBSCRIPTION:
            case ASK_TIME_BY_SUBSCRIPTION:
            case ADD_SUBSCRIPTION:
            case DELETE_SUBSCRIPTION:
            case GET_SUBSCRIPTION_BY_CITY:
            case GET_SUBSCRIPTION_BY_TIME:
            case SEND:
                return true;
            default:
                return false;
        }
    }
}
