package org.komarichyn.telegram.bot;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


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
