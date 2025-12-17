package org.telegram.examplebots;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


public class Application {
    private static final String BOT_TOKEN = "BOT_TOKEN";
    private static final String BOT_USERNAME = "BOT_USERNAME";

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    static void main() {
        // var bot = new HelloBot(BOT_TOKEN, BOT_USERNAME);
        var bot = new ExampleBot(BOT_TOKEN, BOT_USERNAME);
        bot.onRegister();

        //noinspection resource
        var botsApplication = new TelegramBotsLongPollingApplication();
        try {
            botsApplication.registerBot(BOT_TOKEN, bot);
        } catch (TelegramApiException e) {
            log.error("Oops, something went wrong while registering bot", e);
        }

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                botsApplication.close();
            } catch (Exception e) {
                log.error("Oops, something went wrong while closing application", e);
            }
        }));
    }
}
