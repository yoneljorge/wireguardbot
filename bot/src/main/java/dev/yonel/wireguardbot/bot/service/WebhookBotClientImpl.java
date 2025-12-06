
package dev.yonel.wireguardbot.bot.service;

import java.util.List;

import jakarta.annotation.PostConstruct;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import dev.yonel.wireguardbot.bot.properties.TelegramBotClientProperties;
import dev.yonel.wireguardbot.common.dtos.telegram.ResponseBody;
import dev.yonel.wireguardbot.common.enums.TypeWebhookTelegramBot;
import dev.yonel.wireguardbot.common.events.MessageRelayToTelegramBotClientEvent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class WebhookBotClientImpl extends BaseTelegramWebhookBot implements WebhookBotClient{

    private final RegisterWebhookService registerWebhook;

    public WebhookBotClientImpl(TelegramBotClientProperties properties, RegisterWebhookService registerWebhook) {
        super(properties);
        this.registerWebhook = registerWebhook;
    }

    @PostConstruct
    public void init(){
        registerWebhook.registerWebhook(this);
    }

    @Async
    @EventListener
    public void handleMessageRelayToTelegramBotClientEvent(MessageRelayToTelegramBotClientEvent event) {
        log.info("Evento ResponseToTelegramEvent recibido.");
        List<ResponseBody> responseBody = event.getResponses();
        try {
            List<Object> response = convertToObject(telegramPlatform.onReceivedEventFromMessageRelay(responseBody));
            handleResponse(response, TypeWebhookTelegramBot.CLIENT);
        } catch (Exception e) {
            log.error("Error al manejar handleMessageRelayToTelegramBotClientEvent: {}", e.getMessage());
            e.printStackTrace();
        }
    }
}