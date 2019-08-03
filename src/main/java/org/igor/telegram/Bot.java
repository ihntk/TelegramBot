package org.igor.telegram;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Bot extends TelegramLongPollingBot {
    private String botUsername = "Scrooge";
    private String botToken = "913380434:AAFDoIO8-WI8Xmxhpgc4WUm3vGZpZ7R87jg";

    public void onUpdateReceived(Update update) {
        String message = update.getMessage().getText();
        String answer = null;

        if (message.startsWith("/uname"))
            answer = handleUname(message.substring(message.indexOf("/uname") + 6).trim());

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
        answer = answer.replaceAll("#","_").replaceAll("@","-");
        return answer;
    }

    public String getBotUsername() {
        return botUsername;
    }

    public String getBotToken() {
        return botToken;
    }
}
