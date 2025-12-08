package dev.yonel.wireguardbot.bot.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dev.yonel.wireguardbot.common.dtos.telegram.ResponseBody;
import dev.yonel.wireguardbot.common.enums.TypeSendExecution;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class MessageTypeSelector {

    @Autowired
    private TelegramResponsesBuilder messageBuilder;

    /**
     * Selecciona automáticamente el tipo de mensaje basándose en el ResponseBody.
     * 
     * Prioridad de detección:
     * 1. Sí hay un File en el ResponseBody -> SEND_DOCUMENT
     * 2. Sí hay un typeSendExecution explícito -> usar ese tipo
     * 3. Por defecto -> SEND_MESSAGE
     * 
     * @param responseBody El ResponseBody a procesar
     * @return El objeto de mensaje construido (CustomSendMessage, CustomSendDocument, etc.)
     */
    public Object selectAndBuild(ResponseBody responseBody) {
        // Prioridad 1: Detección por presencia de archivo
        if (responseBody.getFile() != null) {
            log.debug("Archivo detectado en ResponseBody, construyendo CustomSendDocument");
            return messageBuilder.buildSendDocument(responseBody);
        }

        // Prioridad 2: Detección por typeSendExecution explícito
        if (responseBody.getTypeSendExecution() != null) {
            return buildByTypeExecution(responseBody);
        }

        // Prioridad 3: Por defecto, mensaje de texto
        log.debug("Ninguna condición especial detectada, construyendo CustomSendMessage por defecto");
        return messageBuilder.buildSendMessage(responseBody);
    }

    /**
     * Construye el mensaje basándose en el TypeSendExecution especificado.
     */
    private Object buildByTypeExecution(ResponseBody responseBody) {
        TypeSendExecution type = responseBody.getTypeSendExecution();
        
        switch (type) {
            case SEND_DOCUMENT:
                log.debug("TypeSendExecution.SEND_DOCUMENT detectado");
                return messageBuilder.buildSendDocument(responseBody);
                
            case SEND_MESSAGE:
                log.debug("TypeSendExecution.SEND_MESSAGE detectado");
                return messageBuilder.buildSendMessage(responseBody);
                
            case EDIT_MESSAGE_TEXT:
                log.debug("TypeSendExecution.EDIT_MESSAGE_TEXT detectado");
                // Nota: Este requiere un messageId, que debería venir en el ResponseBody
                if (responseBody.getMessageId() != null) {
                    return messageBuilder.buildEditMessageText(responseBody, responseBody.getMessageId());
                } else {
                    log.warn("EDIT_MESSAGE_TEXT requiere messageId, pero no está presente. Usando SEND_MESSAGE como fallback.");
                    return messageBuilder.buildSendMessage(responseBody);
                }
                
            case EDIT_MESSAGE_REPLY_MARKUP:
                log.debug("TypeSendExecution.EDIT_MESSAGE_REPLY_MARKUP detectado");
                // Similar al anterior, requiere messageId y chatId
                if (responseBody.getMessageId() != null && responseBody.getChatid() != null) {
                    return messageBuilder.buildEditMessageReplyMarkup(
                        responseBody, 
                        responseBody.getMessageId(), 
                        responseBody.getChatid()
                    );
                } else {
                    log.warn("EDIT_MESSAGE_REPLY_MARKUP requiere messageId y chatId, pero no están presentes. Usando SEND_MESSAGE como fallback.");
                    return messageBuilder.buildSendMessage(responseBody);
                }
                
            default:
                log.warn("TypeSendExecution '{}' no está implementado aún, usando SEND_MESSAGE como fallback.", type);
                return messageBuilder.buildSendMessage(responseBody);
        }
    }
}

