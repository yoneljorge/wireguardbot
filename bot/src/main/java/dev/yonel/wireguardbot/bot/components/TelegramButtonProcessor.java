
package dev.yonel.wireguardbot.bot.components;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import dev.yonel.wireguardbot.bot.components.custom.CustomEditMessageReplyMarkup;
import dev.yonel.wireguardbot.bot.components.custom.CustomEditMessageText;
import dev.yonel.wireguardbot.bot.components.custom.CustomSendMessage;
import dev.yonel.wireguardbot.bot.service.queue.MensajeColaService;
import dev.yonel.wireguardbot.common.dtos.telegram.MessageBody;
import dev.yonel.wireguardbot.common.dtos.telegram.ResponseBody;
import dev.yonel.wireguardbot.common.enums.TypeWebhookTelegramBot;
import dev.yonel.wireguardbot.common.services.message_manager.MessageRelayService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class TelegramButtonProcessor {

    @Autowired
    private MessageRelayService messageRelayService;

    @Autowired
    private TelegramResponsesBuilder responsesBuilder;

    @Autowired
    private TelegramUserManager userManager;

    public List<CustomEditMessageText> processEditMessageText(TypeWebhookTelegramBot bot, Update update, String fileUrl) throws Throwable {
        if (update.getCallbackQuery() == null) {
            log.warn("No se encontró callbackQuery.");
            return null;
        }

        MessageBody messageBody = userManager.createMessageBody(bot, update, fileUrl);
        List<ResponseBody> responses = messageRelayService.handleMessage(messageBody);

        if (responses == null || responses.isEmpty()) {
            log.warn("Respuesta vacía para edición de texto.");
            return null;
        }

        List<CustomEditMessageText> messages = new ArrayList<>();

        for (ResponseBody resp : responses) {
            messages.add(
                    responsesBuilder.buildEditMessageText(resp, update.getCallbackQuery().getMessage().getMessageId()));
        }

        MensajeColaService.removeMensajesProcesados(bot, String.valueOf(messageBody.getUserid()),
                messageBody.getUpdateid());

        return messages;
    }

    public List<CustomEditMessageReplyMarkup> processEditButtons(Update update, ResponseBody responseBody) {
        try {
            return List.of(responsesBuilder.buildEditMessageReplyMarkup(responseBody,
                    update.getCallbackQuery().getMessage().getMessageId(),
                    update.getCallbackQuery().getMessage().getChatId()));
        } catch (Exception e) {
            log.error("Error al editar los botones: ", e);
            return null;
        }
    }

    public List<CustomEditMessageReplyMarkup> processRemoveButtons(Update update, ResponseBody responseBody) {
        try {
            return List.of(responsesBuilder.buildEditMessageReplyMarkup(responseBody,
                    update.getCallbackQuery().getMessage().getMessageId(),
                    update.getCallbackQuery().getMessage().getChatId()));
        } catch (Exception e) {
            log.error("Error al eliminar los botones: ", e);
            return null;
        }
    }
}