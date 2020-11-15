package com.home.MyWizardBot.appConfig;

import com.home.MyWizardBot.MyWizardTelegramBot;
import com.home.MyWizardBot.botApi.TelegramFacade;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.ApiContext;
@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "telegrambot")
public class BotConfig {

    private String webHookPath;
    private String botUserName;
    private String botToken;


    @Bean
    public MyWizardTelegramBot myWizardTelegramBot(TelegramFacade telegramFacade) {

        MyWizardTelegramBot myCommunicationTelegramBot = new MyWizardTelegramBot(telegramFacade);
        myCommunicationTelegramBot.setBotUserName(botUserName);
        myCommunicationTelegramBot.setBotToken(botToken);
        myCommunicationTelegramBot.setWebHookPath(webHookPath);

        return myCommunicationTelegramBot;
    }
    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();

        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

}
