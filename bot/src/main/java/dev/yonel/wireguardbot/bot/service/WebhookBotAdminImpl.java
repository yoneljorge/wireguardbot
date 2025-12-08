package dev.yonel.wireguardbot.bot.service;


import java.util.List;

import jakarta.annotation.PostConstruct;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import dev.yonel.wireguardbot.bot.properties.TelegramBotAdminProperties;
import dev.yonel.wireguardbot.common.dtos.telegram.ResponseBody;
import dev.yonel.wireguardbot.common.enums.TypeWebhookTelegramBot;
import dev.yonel.wireguardbot.common.events.MessageRelayToTelegramBotAdminEvent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class WebhookBotAdminImpl extends BaseTelegramWebhookBot implements WebhookBotAdmin{

    private final RegisterWebhookService registerWebhook;
    public WebhookBotAdminImpl(TelegramBotAdminProperties properties, RegisterWebhookService registerWebhook) {
        super(properties);
        this.registerWebhook = registerWebhook;
    }

    @PostConstruct
    public void init(){
        registerWebhook.registerWebhook(BOT_TOKEN, BOT_PATH);
    }

    @Async
    @EventListener
    public void handleMessageRelayToTelegramBotAdminEvent(MessageRelayToTelegramBotAdminEvent event) {
        log.info("Evento ResponseToTelegramEvent recibido.");
        List<ResponseBody> responsesBody = event.getResponses();
        try {
            List<Object> response = convertToObject(telegramPlatform.onReceivedEventFromMessageRelay(responsesBody));
            handleResponse(response, TypeWebhookTelegramBot.ADMIN);
        } catch (Exception e) {
            log.error("Error al manejar handleMessageRelayToTelegramBotAdminEvent: {}", e.getMessage());
            e.printStackTrace();
        }
    }

    
}
