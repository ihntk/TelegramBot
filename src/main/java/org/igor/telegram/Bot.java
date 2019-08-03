package org.igor.telegram;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Bot extends TelegramLongPollingBot {
    private final String BOT_USERNAME;
    private final String BOT_TOKEN;
    private Dialog activeDialog;

    public Bot() {
        BOT_USERNAME = Main.getProperties().getProperty("BOT_USERNAME");
        BOT_TOKEN = Main.getProperties().getProperty("BOT_TOKEN");
    }

    public void onUpdateReceived(Update update) {
        String message = update.getMessage().getText();
        String answer = null;

        if (activeDialog != null)
            answer = activeDialog.handleDialog(message);

        if (message.startsWith("/uname"))
            answer = handleUname(message.substring(message.indexOf("/uname") + 6).trim());

        if (message.startsWith("/torrent"))
            answer = new TorrentDialog().handleDialog(message);

        sendMsg(update.getMessage().getChatId(), answer == null ? message : answer);
    }

    private synchronized void sendMsg(Long chatId, String message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId);
        sendMessage.setText(message);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private String handleUname(String message) {
        String answer = null;
        try {
            Process process = null;
            process = Runtime.getRuntime().exec("uname -a");

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            answer = bufferedReader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        answer = answer.replaceAll("#", "_").replaceAll("@", "-");
        return answer;
    }

    public String getBotUsername() {
        return BOT_USERNAME;
    }

    public String getBotToken() {
        return BOT_TOKEN;
    }

    public void setActiveDialog(Dialog activeDialog) {
        this.activeDialog = activeDialog;
    }
}
