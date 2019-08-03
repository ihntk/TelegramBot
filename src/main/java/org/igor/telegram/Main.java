package org.igor.telegram;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Main {
    private static Properties properties;
    private static Bot bot;
    private static final String CONF_FILE = ".config/telegramBot/telegramBot.conf";

    public Main() throws IOException {
        properties = new Properties();
        properties.load(new FileInputStream(System.getProperty("user.home") + "/" + CONF_FILE));
    }

    public static void main(String[] args) throws IOException {
        new Main();
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            bot = new Bot();
            telegramBotsApi.registerBot(bot);
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }
    }

    public static Bot getBot() {
        return bot;
    }

    public static Properties getProperties() {
        return properties;
    }
}
