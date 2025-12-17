<div align="center">
  <img src="https://github.com/addo37/AbilityBots/blob/gh-pages/images/API%20BOT-03.png?raw=true" alt="abilitybots" width="200" height="200"/>

[![Jitpack](https://jitpack.io/v/addo37/ExampleBots.svg)](https://jitpack.io/#addo37/ExampleBots)
[![Telegram](https://telegram-badge.vercel.app/api/telegram-badge?channelId=@AbilityBots)](https://telegram.me/AbilityBots)
</div>

# Example Bots

Small, up-to-date samples of [telegrambots](https://github.com/rubenlagus/TelegramBots) 9.2 ability bots built with Java 25.

## Quick start

- Set `BOT_TOKEN` and `BOT_USERNAME` in `src/main/java/org/telegram/examplebots/Application.java`, then choose whether to register `ExampleBot` or the minimal `HelloBot`.
- Run `./gradlew test` to ensure the project compiles and the mocked sender test still passes.
- Launch `Application.main()` from your IDE (or wire the class into your own host) to start long polling your bot.

## What's inside

- `ExampleBot` shows admin/public abilities, forced replies, photo-only handlers, and `/count` storage using the AbilityBot database. Built-in AbilityBot commands such as `/claim`, `/commands`, `/backup`, etc. remain available out of the box.
- `HelloBot` is a bare-bones hello-world ability for stripping the setup down to the essentials.
- `ExampleBotTest` demonstrates how to plug in Mockito and the `SilentSender` to unit test your abilities without calling Telegram.
