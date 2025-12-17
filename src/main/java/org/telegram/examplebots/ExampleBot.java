package org.telegram.examplebots;

import com.google.common.annotations.VisibleForTesting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.abilitybots.api.bot.AbilityBot;
import org.telegram.telegrambots.abilitybots.api.objects.Ability;
import org.telegram.telegrambots.abilitybots.api.sender.SilentSender;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import java.util.Map;
import java.util.function.Predicate;

import static org.telegram.telegrambots.abilitybots.api.objects.Flag.MESSAGE;
import static org.telegram.telegrambots.abilitybots.api.objects.Flag.PHOTO;
import static org.telegram.telegrambots.abilitybots.api.objects.Flag.REPLY;
import static org.telegram.telegrambots.abilitybots.api.objects.Locality.ALL;
import static org.telegram.telegrambots.abilitybots.api.objects.Locality.USER;
import static org.telegram.telegrambots.abilitybots.api.objects.Privacy.ADMIN;
import static org.telegram.telegrambots.abilitybots.api.objects.Privacy.PUBLIC;

public class ExampleBot extends AbilityBot {

    private static final Logger log = LoggerFactory.getLogger(ExampleBot.class);

    public ExampleBot(String botToken, String botUsername) {
        super(new OkHttpTelegramClient(botToken), botUsername);
    }

    @Override
    public long creatorId() {
        return 330178816; // Your ID here
    }

    public Ability saysHelloWorld() {
        return Ability.builder()
                .name("hello") // Name and command (/hello)
                .info("Says hello world!") // Necessary if you want it to be reported via /commands
                .privacy(PUBLIC)  // Choose from Privacy Class (PUBLIC, GROUP_ADMIN, ADMIN, CREATOR)
                .locality(ALL) // Choose from Locality enum Class (USER, GROUP, ALL)
                .input(0) // Arguments required for command (0 for ignore)
                .action(ctx -> {
                    /*
                    ctx has the following main fields that you can utilize:
                    - ctx.update() -> the actual Telegram update from the basic API
                    - ctx.user() -> the user behind the update
                    - ctx.firstArg()/secondArg()/thirdArg() -> quick accessors for message arguments (if any)
                    - ctx.arguments() -> all arguments
                    - ctx.chatId() -> the chat where the update has emerged

                    NOTE that chat ID and user are fetched no matter what the update carries.
                    If the update does not have a message, but it has a callback query, the chatId and user will be fetched from that query.
                    */
                    // Custom sender implementation
                    silent.send("Hello World!", ctx.chatId());
                })
                .build();
    }

    /**
     * Says hi with the specified argument.
     * <p>
     * You can experiment by using /sayhi developer. You can also try not supplying it an argument. :)
     * <p>
     * Note that this ability only works in USER locality mode. So, it won't work in groups!
     */
    public Ability saysHelloWorldToFriend() {
        return Ability.builder()
                .name("sayhi")
                .info("Says hi")
                .privacy(PUBLIC)
                .locality(USER)
                .input(1)
                .action(ctx -> silent.send("Hi " + ctx.firstArg(), ctx.chatId()))
                .build();
    }


    /**
     * Admin-only ability. Use /scared to invoke in any chat.
     * <p>
     * To use this ability, do /claim then /scared! /claim will make you the creator of the bot and automatically an admin.
     */
    public Ability scaredOfAdmins() {
        return Ability.builder()
                .name("scared")
                .info("Makes me scared of admins!")
                .privacy(ADMIN)
                .locality(ALL)
                .input(0)
                .action(ctx -> {
                    // Custom sender implementation
                    silent.send("Eeeeek, I'm so sorry your highness!", ctx.chatId());
                })
                .post(ctx -> {
                    // This "post" action is executed after the main action is done
                    silent.send("I will never do it again!", ctx.chatId());
                })
                .build();
    }

    /**
     * This is very important to override for {@link ExampleBot#sayNiceToPhoto()}. By default, any update that does not have a message will not pass through abilities.
     * To customize that, you can just override this global flag and make it return true at every update.
     * This way, the ability flags will be the only ones responsible for checking the update's validity.
     */
    @Override
    public boolean checkGlobalFlags(Update update) {
        return true;
    }

    /**
     * This ability has an extra "flag". It needs a photo to activate! This feature is activated by default if there is no /command given.
     */
    public Ability sayNiceToPhoto() {
        return Ability.builder()
                .name(DEFAULT) // DEFAULT ability is executed if user did not specify a command -> Bot needs to have access to messages (check FatherBot)
                .flag(PHOTO)
                .privacy(PUBLIC)
                .locality(ALL)
                .input(0)
                .action(ctx -> silent.send("Daaaaang, what a nice photo!", ctx.chatId()))
                .build();
    }

    /**
     * Use the database to fetch a count per user and increments.
     * <p>
     * Use /count to experiment with this ability.
     */
    public Ability useDatabaseToCountPerUser() {
        return Ability.builder()
                .name("count")
                .info("Increments a counter per user")
                .privacy(PUBLIC)
                .locality(ALL)
                .input(0)
                .action(ctx -> {
                    // db.getMap takes in a string, this must be unique and the same everytime you want to call the exact same map
                    // TODO: Using integer as a key in this db map is not recommended, it won't be serialized/deserialized properly if you ever decide to recover/backup db
                    Map<String, Integer> countMap = db.getMap("COUNTERS");
                    long userId = ctx.user().getId();

                    // Get and increment counter, put it back in the map
                    Integer counter = countMap.compute(String.valueOf(userId), (id, count) -> count == null ? 1 : ++count);

                    /*

                    Without lambdas implementation of incrementing

                    int counter;
                    if (countMap.containsKey(userId))
                    counter = countMap.get(userId) + 1;
                    else
                    counter = 1;
                    countMap.put(userId, counter);

                    */

                    // Send formatted will enable markdown
                    String message = String.format("%s, your count is now *%d*!", ctx.user().getUserName(), counter);
                    silent.sendMd(message, ctx.chatId());
                })
                .build();

        // In this ability, you can also experiment with /backup and /recover of the AbilityBot!
        // Take a backup of when the counter is at 1, do /count multiple times and attempt to recover
        // The counter will be reset since the db will recover to the backup that you specify
    }

    public Ability playWithMe() {
        String playMessage = "Play with me!";

        return Ability.builder()
                .name("play")
                .info("Do you want to play with me?")
                .privacy(PUBLIC)
                .locality(ALL)
                .input(0)
                .action(ctx -> silent.forceReply(playMessage, ctx.chatId()))
                // The signature of a reply is -> (Consumer<Update> action, Predicate<Update>... conditions)
                // So, we  first declare the action that takes an update (NOT A MESSAGECONTEXT) like the action above
                // The reason of that is that a reply can be so versatile depending on the message, context becomes an inefficient wrapping
                .reply((_, upd) -> {
                            // Prints to console
                            log.info("I'm in a reply!");
                            // Sends message
                            silent.send("It's been nice playing with you!", upd.getMessage().getChatId());
                        },
                        // Now we start declaring conditions, MESSAGE is a member of the enum Flag class
                        // That class contains out-of-the-box predicates for your replies!
                        // MESSAGE means that execute the reply if it has a message
                        MESSAGE,
                        // REPLY means that the update must be a reply
                        REPLY,
                        // The reply must be to the bot
                        isReplyToBot(),
                        // The reply is to the playMessage
                        isReplyToMessage(playMessage)
                )
                // You can add more replies by calling .reply(...)
                .build();

    /*
    The checks are made so that, once you execute your logic there is not need to check for the validity of the reply.
    They were all made once the action logic is being executed.
     */
    }

    private Predicate<Update> isReplyToMessage(String message) {
        return upd -> {
            Message reply = upd.getMessage().getReplyToMessage();
            return reply.hasText() && reply.getText().equalsIgnoreCase(message);
        };
    }

    private Predicate<Update> isReplyToBot() {
        return upd -> upd.getMessage().getReplyToMessage().getFrom().getUserName().equalsIgnoreCase(getBotUsername());
    }

    @VisibleForTesting
    void setSender(SilentSender silent) {
        this.silent = silent;
    }
}
