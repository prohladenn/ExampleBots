package org.telegram.examplebots;

import org.telegram.telegrambots.abilitybots.api.bot.AbilityBot;
import org.telegram.telegrambots.abilitybots.api.objects.Ability;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;

import static org.telegram.telegrambots.abilitybots.api.objects.Locality.ALL;
import static org.telegram.telegrambots.abilitybots.api.objects.Privacy.PUBLIC;

public class HelloBot extends AbilityBot {

    public HelloBot(String botToken, String botUsername) {
        super(new OkHttpTelegramClient(botToken), botUsername);
    }

    @Override
    public long creatorId() {
        return 123456789;
    }

    public Ability sayHelloWorld() {
        return Ability
                .builder()
                .name("hello")
                .info("says hello world!")
                .locality(ALL)
                .privacy(PUBLIC)
                .action(ctx -> silent.send("Hello world!", ctx.chatId()))
                .build();
    }
}
