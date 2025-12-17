package org.telegram.examplebots;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.telegram.telegrambots.abilitybots.api.objects.MessageContext;
import org.telegram.telegrambots.abilitybots.api.sender.SilentSender;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

public class ExampleBotTest {

    public static final int USER_ID = 1337;
    public static final long CHAT_ID = 1337L;

    private ExampleBot bot;
    private SilentSender sender;

    @Before
    public void setUp() {
        bot = new ExampleBot("TEST_TOKEN", "TEST_USERNAME");
        sender = mock(SilentSender.class);

        bot.setSender(sender);
    }

    @Test
    public void canSayHelloWorld() {
        Update mockedUpdate = mock(Update.class);
        User user = new User((long) USER_ID, "Abbas", false);
        user.setLastName("Abou Daya");
        user.setUserName("addo37");
        MessageContext context = MessageContext.newContext(mockedUpdate, user, CHAT_ID, bot);

        bot.saysHelloWorld().action().accept(context);

        // We verify that the sender was called only ONCE and sent Hello World to CHAT_ID
        Mockito.verify(sender, times(1)).send("Hello World!", CHAT_ID);
    }
}
