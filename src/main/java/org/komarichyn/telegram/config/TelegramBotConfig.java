package org.komarichyn.telegram.config;

import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.komarichyn.telegram.bot.SmartHouseBot;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;


@Slf4j
@Configuration
public class TelegramBotConfig {

  @Value("${telegram.bot.name}")
  private String botUsername;
  @Value("${telegram.bot.token}")
  private String botToken;

  TelegramBotsApi botsApi = null;


  @Bean
  @Singleton
  public TelegramBotsApi getBot(){
    if(botsApi == null) {
      try {
        botsApi = new TelegramBotsApi(DefaultBotSession.class);

        SmartHouseBot smartHouseBot = new SmartHouseBot();
        smartHouseBot.setBotToken(this.botToken);
        smartHouseBot.setBotUsername(this.botUsername);

        botsApi.registerBot(smartHouseBot);

      } catch (TelegramApiException e) {
        log.error(e.getMessage(), e);
      }
    }
    return botsApi;
  }

}
