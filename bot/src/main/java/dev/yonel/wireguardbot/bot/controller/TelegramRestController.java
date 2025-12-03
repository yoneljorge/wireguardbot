
package dev.yonel.wireguardbot.bot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.objects.Update;

import dev.yonel.wireguardbot.bot.properties.TelegramBotAdminProperties;
import dev.yonel.wireguardbot.bot.properties.TelegramBotClientProperties;
import dev.yonel.wireguardbot.bot.service.queue.IncomingMessageTelegram;
import dev.yonel.wireguardbot.bot.service.queue.MensajeColaService;
import dev.yonel.wireguardbot.common.enums.TypeWebhookTelegramBot;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/webhook-telegram")
public class TelegramRestController {

    @Autowired
    private MensajeColaService colaService;

    @Autowired
    private TelegramBotClientProperties telegramBotClientProperties;

    @Autowired
    private TelegramBotAdminProperties telegramBotAdminProperties;

    @PostMapping("{secret}")
    public ResponseEntity<?> onUpdateReceived(@PathVariable String secret, @RequestBody Update update) {
        if (!secret.equals(telegramBotClientProperties.getToken())
                && !secret.equals(telegramBotAdminProperties.getToken())) {
                    log.info("recibiendo");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if (secret.equals(telegramBotClientProperties.getToken())) {
            try {

                colaService.encolarMensaje(new IncomingMessageTelegram(update, TypeWebhookTelegramBot.CLIENT));

                return ResponseEntity.ok(null);
            } catch (Exception e) {
                log.error("Error al procesar la actualizacion del bot client", e);
                return null;
            }
        } else if (secret.equals(telegramBotAdminProperties.getToken())) {
            try {

                colaService.encolarMensaje(new IncomingMessageTelegram(update, TypeWebhookTelegramBot.ADMIN));

                return ResponseEntity.ok(null);
            } catch (Exception e) {
                log.error("Error al procesar la actualizacion del bot administrador.", e);
                return null;
            }
        }

        return ResponseEntity.ok(null);
    }
}
