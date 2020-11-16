package com.home.WeatherBot.appConfig;

import com.home.WeatherBot.WeatherTelegramBot;
import com.home.WeatherBot.botApi.TelegramFacade;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "telegrambot")
public class BotConfig {

    private String webHookPath;
    private String botUserName;
    private String botToken;


    @Bean
    public WeatherTelegramBot weatherTelegramBot(TelegramFacade telegramFacade) {

        WeatherTelegramBot weatherTelegramBot = new WeatherTelegramBot(telegramFacade);
        weatherTelegramBot.setBotName(botUserName);
        weatherTelegramBot.setBotToken(botToken);
        weatherTelegramBot.setWebHookPath(webHookPath);

        return weatherTelegramBot;
    }
    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();

        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

}
