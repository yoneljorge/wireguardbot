
package dev.yonel.wireguardbot.bot.components;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import dev.yonel.wireguardbot.bot.components.custom.CustomSendMessage;
import dev.yonel.wireguardbot.bot.service.queue.MensajeColaService;
import dev.yonel.wireguardbot.common.dtos.telegram.MessageBody;
import dev.yonel.wireguardbot.common.dtos.telegram.ResponseBody;
import dev.yonel.wireguardbot.common.enums.TypeWebhookTelegramBot;
import dev.yonel.wireguardbot.common.services.message_manager.MessageRelayService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class TelegramMessageProcessor {

    @Autowired
    private MessageRelayService messageRelayService;

    @Autowired
    private TelegramMessageBuilder messageBuilder;

    @Autowired
    private TelegramUserManager userManager;

    public List<CustomSendMessage> processTextMessage(Update update,
            TypeWebhookTelegramBot bot) throws Throwable {
        if (update.getMessage() == null || update.getMessage().getText() == null) {
            log.warn("Update no contiene mensaje de texto.");
            return null;
        }

        MessageBody messageBody = userManager.createMessageBodyFromMessage(update, bot);
        List<ResponseBody> responses = messageRelayService.handleMessage(messageBody);

        if (responses == null || responses.isEmpty()) {
            log.warn("No se recibió respuesta válida para el mensaje.");
            return null;
        }

        List<CustomSendMessage> messages = new ArrayList<>();

        for (ResponseBody resp : responses) {
            messages.add(messageBuilder.buildSendMessage(resp));
            // FIXME: solo para pruebas
            // System.out.println(resp.getResponse());
        }

        MensajeColaService.removeMensajesProcesados(bot, String.valueOf(messageBody.getUserid()),
                messageBody.getUpdateid());
        return messages;
    }

    public List<CustomSendMessage> processImageMessage(Update update, String fileUrl,
            TypeWebhookTelegramBot bot)
            throws Throwable {
        if (update.getMessage() == null) {
            log.warn("Update no contiene mensaje.");
            return null;
        }

        MessageBody messageBody = userManager.createMessageBodyFromImageMessage(update, fileUrl, bot);
        List<ResponseBody> responses = messageRelayService.handleMessage(messageBody);

        if (responses == null || responses.isEmpty()) {
            log.warn("No se recibió respuesta válida para el mensaje.");
            return null;
        }

        List<CustomSendMessage> messages = new ArrayList<>();

        for (ResponseBody resp : responses) {
            messages.add(messageBuilder.buildSendMessage(resp));
        }

        MensajeColaService.removeMensajesProcesados(bot, String.valueOf(messageBody.getUserid()),
                messageBody.getUpdateid());
        return messages;
    }

    @Autowired
    private MessageTypeSelector messageTypeSelector;

    public List<Object> processResponseFromMessageRelay(List<ResponseBody> responses) {
        if (responses == null || responses.isEmpty()) {
            log.warn("No se recibió respuesta válida para el mensaje.");
            return null;
        }
        
        List<Object> messages = new ArrayList<>();

        for (ResponseBody resp : responses) {
            Object message = messageTypeSelector.selectAndBuild(resp);
            if(message != null){}
            messages.add(message);
        }

        return messages;
    }
}