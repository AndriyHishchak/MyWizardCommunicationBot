package com.home.WeatherBot.botApi;

import com.home.WeatherBot.Service.MainMenuService;
import com.home.WeatherBot.Service.ReplyMessagesService;
import com.home.WeatherBot.cache.BotDataCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;

@Component
@Slf4j
public class TelegramFacade {

    private final BotStateContext botStateContext;
    private final BotDataCache botDataCache;
    private final MainMenuService mainMenuService;
    private final ReplyMessagesService messagesService;

    public TelegramFacade(BotStateContext botStateContext, BotDataCache botDataCache, MainMenuService mainMenuService, ReplyMessagesService messagesService) {
        this.botStateContext = botStateContext;
        this.botDataCache = botDataCache;
        this.mainMenuService = mainMenuService;
        this.messagesService = messagesService;

    }

    public BotApiMethod<?> handleUpdate(Update update) throws IOException, TelegramApiException {
        SendMessage replyMessage = null;

        if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            log.info("New callbackQuery from User: {}, userId: {}, with data: {}", update.getCallbackQuery().getFrom().getUserName(),
                    callbackQuery.getFrom().getId(), update.getCallbackQuery().getData());
            return processCallbackQuery(callbackQuery);
        }

        Message message = update.getMessage();
        if(message !=null && message.hasText()) {
           log.info("New message from User: {}, chatId: {}, with text: {}",
                   message.getFrom().getUserName(),
                   message.getChatId(),
                   message.getText());
           replyMessage = handleInputMessage(message);
        }
        return replyMessage;
    }


    private SendMessage handleInputMessage(Message message) throws IOException {
        String inputMessage = message.getText();
        long userId = message.getFrom().getId();
        BotState botState;
        SendMessage replyMessage;

        switch (inputMessage) {
            case "/start":
                botState = BotState.WELCOME_MENU;
                break;
            case "\uD83C\uDF26 Show weather":
                botState = BotState.FILLING_WEATHER_REQUEST;
                break;
            case "\uD83D\uDCC6 Subscribe":
                botState = BotState.ADD_SUBSCRIPTION;
                break;
            case "❌ Unsubscribe":
                botState = BotState.DELETE_SUBSCRIPTION;
                break;
            case "\uD83D\uDCAC Help":
                botState = BotState.SHOW_HELP_MENU;
                break;
            default:
                botState = botDataCache.getUsersCurrentBotState(userId);
                break;
        }
        botDataCache.setUsersCurrentBotState(userId,botState);
        replyMessage = botStateContext.processInputMessage(botState,message);
        return replyMessage;
    }

    private BotApiMethod<?> processCallbackQuery(CallbackQuery buttonQuery) throws TelegramApiException {
        final long chatId = buttonQuery.getMessage().getChatId();
        final long userId = buttonQuery.getFrom().getId();
        BotApiMethod<?> callBackAnswer = mainMenuService.getMainMenuMessage(chatId, "Скористуйтеся головним меню");


        switch (buttonQuery.getData()) {
            case "buttonYes":
                callBackAnswer = messagesService.getReplyMessage(chatId, "reply.askName");
                botDataCache.setUsersCurrentBotState(userId, BotState.FILLING_WEATHER_REQUEST);
                break;
            case "buttonNo":
                callBackAnswer = sendAnswerCallbackQuery("Повертайтеся, коли будете готові", false, buttonQuery);
                break;
            case "buttonIwillThink":
                callBackAnswer = sendAnswerCallbackQuery("Дана кнопка не підтримується", true, buttonQuery);
                break;
            case "backMenu":
                botDataCache.setUsersCurrentBotState(userId, BotState.WELCOME_MENU);
                break;
            case "againRain":
                callBackAnswer = messagesService.getReplyMessage(chatId, "reply.askAnswer1");
                botDataCache.setUsersCurrentBotState(userId, BotState.FILLING_WEATHER_REQUEST);
                break;
            default:
                botDataCache.setUsersCurrentBotState(userId, BotState.SHOW_MAIN_MENU);
                break;
        }
        return callBackAnswer;
    }


    private AnswerCallbackQuery sendAnswerCallbackQuery(String text, boolean alert, CallbackQuery callbackquery) throws TelegramApiException {
        AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery();
        answerCallbackQuery.setCallbackQueryId(callbackquery.getId());
        answerCallbackQuery.setShowAlert(alert);
        answerCallbackQuery.setText(text);
        return answerCallbackQuery;
    }

}
