
package dev.yonel.wireguardbot.bot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import dev.yonel.wireguardbot.bot.properties.TelegramBotAdminProperties;
import dev.yonel.wireguardbot.bot.properties.TelegramBotClientProperties;
import dev.yonel.wireguardbot.bot.service.WebhookBotAdminImpl;
import dev.yonel.wireguardbot.bot.service.WebhookBotClientImpl;


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
        //FIXME borrar esto
        System.out.println("=====================");
        System.out.println(telegramBotClientProperties.getToken());

        
        return new WebhookBotClientImpl(telegramBotClientProperties);
    }

    @Bean
    public WebhookBotAdminImpl webhookBotAdminImpl(){
        return new WebhookBotAdminImpl(telegramBotAdminProperties);
    }
}
