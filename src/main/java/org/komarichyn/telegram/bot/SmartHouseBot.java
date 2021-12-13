package org.komarichyn.telegram.bot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

/**
 * Done! Congratulations on your new bot. You will find it at t.me/MK_SmartHouseBot. You can now add a description, about section and profile picture for your bot, see /help for a list of commands. By
 * the way, when you've finished creating your cool bot, ping our Bot Support if you want a better username for it. Just make sure the bot is fully operational before you do this.
 * <p>
 * Use this token to access the HTTP API: 5010264557:AAHN5GtTLQTZEha_co-8A0Ntb1NvmIc_25U Keep your token secure and store it safely, it can be used by anyone to control your bot.
 * <p>
 * For a description of the Bot API, see this page: https://core.telegram.org/bots/api
 */
@Slf4j
public class SmartHouseBot extends TelegramLongPollingBot {

  private String botUsername;
  private String botToken;

  @Override
  public String getBotUsername() {
    return this.botUsername;
  }

  @Override
  public String getBotToken() {
    return this.botToken;
  }

  public void setBotUsername(String botUsername) {
    this.botUsername = botUsername;
  }

  public void setBotToken(String botToken) {
    this.botToken = botToken;
  }

  @Override
  public void onUpdateReceived(Update update) {
    log.debug("Receive new Update. updateID: " + update.getUpdateId());

    Long chatId = update.getMessage().getChatId();
    String inputText = update.getMessage().getText();

    if (inputText.startsWith("/start")) {
      SendMessage message = new SendMessage();
      message.setChatId(chatId.toString());
      message.setText("Hello. This is start message" + update.getUpdateId());
      try {
        execute(message);
      } catch (TelegramApiException e) {
        log.error(e.getMessage(), e);
      }
    }
  }
}
