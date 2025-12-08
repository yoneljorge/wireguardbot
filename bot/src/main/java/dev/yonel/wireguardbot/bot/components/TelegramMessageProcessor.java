
package dev.yonel.wireguardbot.bot.components;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

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
    private TelegramResponsesBuilder messageBuilder;

    @Autowired
    private TelegramUserManager userManager;

    @Autowired
    private MessageTypeSelector messageTypeSelector;

    public List<Object> processRequestMessage(
            TypeWebhookTelegramBot bot,
            Update update,
            String fileUrl) throws Throwable {
        if ((update.getMessage() == null || update.getMessage().getText() == null) && update.getCallbackQuery() == null) {
            log.warn("Update no contiene mensaje de texto ni callbackQuery.");
            return null;
        }


        MessageBody messageBody = userManager.createMessageBody(bot, update, fileUrl);
        List<ResponseBody> responses = messageRelayService.handleMessage(messageBody);

        MensajeColaService.removeMensajesProcesados(bot, String.valueOf(messageBody.getUserid()),
                messageBody.getUpdateid());
        return processResponses(responses);
    }

    public List<Object> processResponses(List<ResponseBody> responses) {
        if (responses == null || responses.isEmpty()) {
            log.warn("No se recibió respuesta válida para el mensaje.");
            return null;
        }
        
        List<Object> messages = new ArrayList<>();

        for (ResponseBody resp : responses) {
            Object message = messageTypeSelector.selectAndBuild(resp);
            if(message != null){
                messages.add(message);
            }
        }
        return messages;
    }
}