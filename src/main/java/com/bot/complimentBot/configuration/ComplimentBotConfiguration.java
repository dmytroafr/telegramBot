package com.bot.complimentBot.configuration;

import com.bot.complimentBot.bot.ComplimentBot;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
public class ComplimentBotConfiguration {
    @Bean
    public TelegramBotsApi telegramBotsApi(ComplimentBot complimentBot) throws TelegramApiException {
        var api = new TelegramBotsApi(DefaultBotSession.class);
        api.registerBot(complimentBot);
        return api;
    }

    @Bean
    public OkHttpClient okHttpClient(){
        return new OkHttpClient();
    }
}
