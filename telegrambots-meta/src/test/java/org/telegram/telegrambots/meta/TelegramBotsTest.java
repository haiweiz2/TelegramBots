package org.telegram.telegrambots.meta;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.BotSession;
import org.telegram.telegrambots.meta.generics.LongPollingBot;
import org.telegram.telegrambots.meta.generics.TelegramBot;
import org.telegram.telegrambots.meta.generics.WebhookBot;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TelegramBotsTest {



    @Test
    public void longPollingBotStopTest() {

        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            TelegramLongPollingBot bot = new MyAmazingBot();
            BotSession botSession = telegramBotsApi.registerBot(bot);
            //bot Running
            assertEquals(true, botSession.isRunning());
            botSession.stop();
            //bot stop
            assertEquals(false, botSession.isRunning());
        } catch (TelegramApiException e) {

        }
    }

    @Test
    public void WebhookBotStopTest() {

        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            TelegramBot bot = new MyWebhookBot();
            Assertions.assertThrows(Exception.class,()->telegramBotsApi.registerBot((LongPollingBot) bot));
        } catch (TelegramApiException e) {

        }


    }


    public class MyAmazingBot extends TelegramLongPollingBot {
        @Override
        public void onUpdateReceived(Update update) {

        }

        @Override
        public String getBotUsername() {
            return "myamazingbot";
        }

        @Override
        public String getBotToken() {
            return "123456789:qwertyuioplkjhgfdsazxcvbnm";
        }
    }

    public class MyWebhookBot implements WebhookBot {

        @Override
        public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
            return null;
        }

        @Override
        public void setWebhook(SetWebhook setWebhook) throws TelegramApiException {
        }

        @Override
        public String getBotPath() {
            return null;
        }

        @Override
        public String getBotUsername() {
            return "myWebhook";
        }

        @Override
        public String getBotToken() {
            return "123456789:TGTGGGGGGGHHU";
        }
    }





}
