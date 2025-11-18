
package dev.yonel.wireguardbot.bot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import dev.yonel.wireguardbot.bot.service.WebhookBotAdminImpl;
import dev.yonel.wireguardbot.bot.service.WebhookBotClientImpl;
import dev.yonel.wireguardbot.common.properties.telegram.TelegramBotAdminProperties;
import dev.yonel.wireguardbot.common.properties.telegram.TelegramBotClientProperties;


@Configuration
public class BeansTelegramBot {

    //@Autowired
    //private TelegramBotCustomOptions telegramBotCustomOptions;

    @Autowired
    private TelegramBotClientProperties telegramBotClientProperties;

    @Autowired
    private TelegramBotAdminProperties telegramBotAdminProperties;

    @Bean
    public WebhookBotClientImpl webhookBotClientImpl() {
        return new WebhookBotClientImpl(telegramBotClientProperties);
    }

    @Bean
    public WebhookBotAdminImpl webhookBotAdminImpl(){
        return new WebhookBotAdminImpl(telegramBotAdminProperties);
    }
}
