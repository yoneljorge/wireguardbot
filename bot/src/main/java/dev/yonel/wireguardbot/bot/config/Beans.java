
package dev.yonel.wireguardbot.bot.config;

import dev.yonel.wireguardbot.bot.service.RegisterWebhookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import dev.yonel.wireguardbot.bot.properties.TelegramBotAdminProperties;
import dev.yonel.wireguardbot.bot.properties.TelegramBotClientProperties;
import dev.yonel.wireguardbot.bot.service.WebhookBotAdminImpl;
import dev.yonel.wireguardbot.bot.service.WebhookBotClientImpl;


@Configuration
public class Beans {

    @Autowired
    private TelegramBotClientProperties telegramBotClientProperties;

    @Autowired
    private RegisterWebhookService registerWebhook;

    @Autowired
    private TelegramBotAdminProperties telegramBotAdminProperties;

    @Bean
    public WebhookBotClientImpl webhookBotClientImpl() {        
        return new WebhookBotClientImpl(telegramBotClientProperties, registerWebhook);
    }

    @Bean
    public WebhookBotAdminImpl webhookBotAdminImpl(){
        return new WebhookBotAdminImpl(telegramBotAdminProperties, registerWebhook);
    }
}
