package dev.yonel.wireguardbot.bot.config;

import org.telegram.telegrambots.bots.DefaultBotOptions;

public class TelegramBotCustomOptions extends DefaultBotOptions {

    public TelegramBotCustomOptions() {
        setMaxThreads(4);
        setProxyType(ProxyType.NO_PROXY);
        setGetUpdatesTimeout(100);
        setGetUpdatesLimit(50);
    }
}
