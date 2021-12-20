package org.komarichyn.telegram.command;

public interface CommandHandler {
  public String getName();
  public void exec();
}
