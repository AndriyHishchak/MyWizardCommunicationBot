package com.home.WeatherBot.Service;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;
@Service
public class LocaleMessagesService {
    private final Locale locale;
    private final MessageSource messageSource;

    public LocaleMessagesService(@Value("ru-RU") String localeTag, MessageSource messageSource) {
        this.locale = Locale.forLanguageTag(localeTag);
        this.messageSource = messageSource;
    }

    public String getMessage(String message) {
        return messageSource.getMessage(message, null, locale);
    }


}
