package org.igor.telegram;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Bot extends TelegramLongPollingBot {
    private final String BOT_USERNAME;
    private final String BOT_TOKEN;
    private Dialog activeDialog;
    private Document document;

    public Bot() {
        BOT_USERNAME = Main.getProperties().getProperty("BOT_USERNAME");
        BOT_TOKEN = Main.getProperties().getProperty("BOT_TOKEN");
    }

    public void onUpdateReceived(Update update) {
        String answer = null;
        String message = null;

        if (update.hasMessage() && update.getMessage().hasText()) {
            message = update.getMessage().getText().trim();

            if (activeDialog != null)
                answer = activeDialog.handleDialog(message);

            if (message.startsWith("/uname"))
                answer = handleUname(message.substring(message.indexOf("/uname") + 6));

            if (message.startsWith("/torrent"))
                if (document != null)
                    answer = new TorrentDialog().handleDialog(document);
                else
                    answer = new TorrentDialog().handleDialog(message);


        } else if (update.hasMessage() && update.getMessage().hasDocument()) {
            document = update.getMessage().getDocument();
            if (activeDialog != null)
                answer = activeDialog.handleDialog(document);
            else {
                if (document.getFileName().endsWith(".torrent")) {
                    answer = "Looks like received file is /torrent";
                }
            }
        }

        sendMsg(update.getMessage().getChatId(), answer == null ? message : answer);
    }

    private synchronized void sendMsg(Long chatId, String message) {
        if (message == null || message.equals(""))
            return;

        message = message.replaceAll("_", "-");
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

    public void setDocument(Document document) {
        this.document = document;
    }
}
