package dev.yonel.wireguardbot.common.properties.telegram;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Configuration
@ConfigurationProperties("telegram.bot.client")
public class TelegramBotClientProperties implements TelegramBotProperties{
    private String token;
    private String username;
    private String path;
}
