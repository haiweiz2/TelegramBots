package org.telegram.telegrambots.meta;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.BotSession;
import org.telegram.telegrambots.meta.generics.LongPollingBot;
import org.telegram.telegrambots.meta.generics.TelegramBot;
import org.telegram.telegrambots.meta.generics.WebhookBot;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import org.telegram.telegrambots.updatesreceivers.DefaultWebhook;


import static org.junit.jupiter.api.Assertions.assertEquals;

// CS427 Issue link: https://github.com/rubenlagus/TelegramBots/issues/433
/**
 * this class is to create Junit tests to test TelegramBots Issue 433
 */
public class TelegramBotsTest {

    /**
     * Before implementing TelegramBots Issue 433,  test to stop a longPollingBot
     * this test shows that TelegramLongPollingBot can stop using botSession.stop() method
     */
    @Test
    public void longPollingBotStopTest() {
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            TelegramLongPollingBot bot = new MyAmazingBot();
            BotSession botSession = telegramBotsApi.registerBot(bot);
            //before botSession.stop(), bot is Running
            assertEquals(true, botSession.isRunning());
            botSession.stop();
            //after botSession.stop(), bot it not Running
            assertEquals(false, botSession.isRunning());
        } catch (TelegramApiException e) {
        }
    }

    /**
     * Before implementing TelegramBots Issue 433,  test to stop a WebhookBot
     * this test shows that a WebhookBot cannot use telegramBotsApi.registerBot(bot) since it is only for a LongpollBot
     * If WebhookBot uses telegramBotsApi.registerBot(bot), it will throw an Exception
     */
    @Test
    public void WebhookBotStopFailTest() {
        DefaultWebhook defaultWebhook = new DefaultWebhook();
        defaultWebhook.setInternalUrl("http://localhost:8887");

        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class, defaultWebhook);
            TelegramBot bot = new MyWebhookBot();
            Assertions.assertThrows(Exception.class,()->telegramBotsApi.registerBot((LongPollingBot) bot));
        } catch (TelegramApiException e) {
        }
    }


    /**
     * After implementing TelegramBots Issue 433,  test to disable a WebhookBot
     * this test shows after adding a new feature, it can use unRegisterBot method to disable a webhookBot
     * adding a WebhookBot to a server, and then unRegister the Bot, checking the status is correct or not
     */
    @Test
    public void WebhookBotStopPassTest() {
        DefaultWebhook defaultWebhook = new DefaultWebhook();
        defaultWebhook.setInternalUrl("http://localhost:8889");
        SetWebhook  mySetWebhook = new SetWebhook();

        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class, defaultWebhook);
            MyWebhookBot bot = new MyWebhookBot();
            assertEquals(false, bot.getIsRegistered());
            //register a Bot
            telegramBotsApi.registerBot(bot, mySetWebhook);
            assertEquals(true, bot.getIsRegistered());

            //unRegister a Bot
            telegramBotsApi.unRegisterBot(bot);
            assertEquals(false, bot.getIsRegistered());
        } catch (TelegramApiException e) {
        }
    }

    /**
     * After implementing TelegramBots Issue 433,  test to disable a WebhookBot
     * this test shows after adding a new feature, it can use unRegisterBot method to disable a webhookBot
     * adding 3 WebhookBot to a server, and then unRegister 1 Bot, checking the status is correct or not
     */
    @Test
    public void WebhookBotStopPass2Test() {
        DefaultWebhook defaultWebhook = new DefaultWebhook();
        defaultWebhook.setInternalUrl("http://localhost:8886");
        SetWebhook  mySetWebhook = new SetWebhook();

        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class, defaultWebhook);
            MyWebhookBot bot1 = new MyWebhookBot();
            MyWebhookBot bot2 = new MyWebhookBot();
            MyWebhookBot bot3 = new MyWebhookBot();
            MyWebhookBot bot4 = new MyWebhookBot();
            telegramBotsApi.registerBot(bot1, mySetWebhook);
            telegramBotsApi.registerBot(bot2, mySetWebhook);
            telegramBotsApi.registerBot(bot3, mySetWebhook);
            telegramBotsApi.registerBot(bot4, mySetWebhook);
            assertEquals(true, bot1.getIsRegistered());
            assertEquals(true, bot2.getIsRegistered());
            assertEquals(true, bot3.getIsRegistered());
            assertEquals(true, bot4.getIsRegistered());

            //only unRegister the bot1
            telegramBotsApi.unRegisterBot(bot1);
            assertEquals(false, bot1.getIsRegistered());
            assertEquals(true, bot2.getIsRegistered());
            assertEquals(true, bot3.getIsRegistered());
            assertEquals(true, bot4.getIsRegistered());
        } catch (TelegramApiException e) {
        }
    }




    /**
     * create MyAmazingBot class to test
     */
    public class MyAmazingBot extends TelegramLongPollingBot {
        @Override
        public void onUpdateReceived(Update update) { }

        @Override
        public String getBotUsername() {
            return "myamazingbot";
        }

        @Override
        public String getBotToken() {
            return "123456789:qwertyuioplkjhgfdsazxcvbnm";
        }
    }

    /**
     * create MyWebhookBot class to test
     */
    public class MyWebhookBot implements WebhookBot {
        boolean isRegistered = false;

        @Override
        public BotApiMethod<?> onWebhookUpdateReceived(Update update) { return null; }

        @Override
        public void setWebhook(SetWebhook setWebhook) throws TelegramApiException { }

        @Override
        public String getBotPath() {
            return "abc";
        }

        @Override
        public String getBotUsername() {
            return "myWebhook";
        }

        @Override
        public String getBotToken() {
            return "123456789:TGTGGGGGGGHHU";
        }

        @Override
        public void onRegister() {
            isRegistered = true;
        }

        @Override
        public void offRegister() {
            isRegistered = false;
        }

        public boolean getIsRegistered() {
            return isRegistered;
        }
    }



}
