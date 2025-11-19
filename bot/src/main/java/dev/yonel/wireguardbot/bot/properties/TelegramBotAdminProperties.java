package dev.yonel.wireguardbot.bot.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Configuration
@ConfigurationProperties("telegram.bot.admin")
public class TelegramBotAdminProperties implements TelegramBotPropertiesInterface{
    private String token;
    private String username;
    private String path;
}
