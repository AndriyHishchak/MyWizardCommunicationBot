package com.home.MyWizardBot.appConfig;

import com.home.MyWizardBot.MyWizardTelegramBot;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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

    //Create bean MyWizardTelegramBot
    @Bean
    public MyWizardTelegramBot MyCommunicationTelegramBot() {
        DefaultBotOptions options = ApiContext.getInstance(DefaultBotOptions.class);

        MyWizardTelegramBot myCommunicationTelegramBot = new MyWizardTelegramBot();
        myCommunicationTelegramBot.setBotUserName(botUserName);
        myCommunicationTelegramBot.setBotToken(botToken);
        myCommunicationTelegramBot.setWebHookPath(webHookPath);

        return myCommunicationTelegramBot;
    }


}
