
package dev.yonel.wireguardbot.bot.components;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.InputFile;

import dev.yonel.wireguardbot.common.dtos.telegram.ResponseBody;
import dev.yonel.wireguardbot.common.enums.TypeCustomKeyboardMarkup;
import dev.yonel.wireguardbot.common.utils.MarkdownMessageBuilder;
import dev.yonel.wireguardbot.bot.components.custom.CustomEditMessageReplyMarkup;
import dev.yonel.wireguardbot.bot.components.custom.CustomEditMessageText;
import dev.yonel.wireguardbot.bot.components.custom.CustomSendDocument;
import dev.yonel.wireguardbot.bot.components.custom.CustomSendMessage;

@Slf4j
@Component
public class TelegramMessageBuilder {

    public CustomSendMessage buildSendMessage(ResponseBody responseBody) {
        CustomSendMessage sendMessage = new CustomSendMessage();
        if (responseBody.getChatid() != null) {
            sendMessage.setChatId(responseBody.getChatid());
        }
        /*
         * En caso de que el chatId sea un null entonces se pasa el userId que es prácticamente lo mismo.
         */
        else if (responseBody.getUserid() != null) {
            sendMessage.setChatId(responseBody.getUserid());
        } else {
            log.error("No se puede construir el mensaje falta chatId o userId");
            throw new IllegalArgumentException("Falta chatId o userId en el ResponseBody.");
        }

        if (responseBody.getParseMode() != null) {
            sendMessage.setParseMode(responseBody.getParseMode().getValue());
            switch (responseBody.getParseMode()) {
                case MARKDOWN:
                    sendMessage.enableMarkdown(true);
                    sendMessage.setText(MarkdownMessageBuilder.escapeMarkdown(responseBody.getResponse()));
                    break;
                case MARKDOWNV2:
                    sendMessage.enableMarkdownV2(true);
                    sendMessage.setText(MarkdownMessageBuilder.escapeMarkdown(responseBody.getResponse()));
                    break;
                case HTML:
                    sendMessage.enableHtml(true);
                    sendMessage.setText(responseBody.getResponse());
                    break;
            }
        } else {
            sendMessage.setText(responseBody.getResponse());
        }

        if (responseBody.getButtons() != null) {
            if (responseBody.getTypeKeyboard() == TypeCustomKeyboardMarkup.REPLY_KEYBOARD) {
                sendMessage.setReplyMarkup(TelegramButtons.getReplyKeyboard(responseBody));
            } else {
                sendMessage.setReplyMarkup(TelegramButtons.getInlineKeyboard(responseBody));
            }

        } else {
            sendMessage.setReplyMarkup(TelegramButtons.removeReplyKeyboard());
        }

        if (responseBody.isRemovable()) {
            sendMessage.setRemovable(true);
        }

        return sendMessage;
    }

    public CustomEditMessageText buildEditMessageText(ResponseBody responseBody, int messageId) {
        CustomEditMessageText newMessage = new CustomEditMessageText();
        newMessage.setChatId(responseBody.getChatid());
        newMessage.setMessageId(messageId);
        newMessage.setText(responseBody.getResponse());

        if (responseBody.getButtons() != null) {
            newMessage.setReplyMarkup(TelegramButtons.getInlineKeyboard(responseBody));
        }

        if (responseBody.isRemovable()) {
            newMessage.setRemovable(true);
        }
        
        return newMessage;
    }

    public CustomEditMessageReplyMarkup buildEditMessageReplyMarkup(ResponseBody responseBody, int messageId,
            long chatId) {
        CustomEditMessageReplyMarkup markup = new CustomEditMessageReplyMarkup();
        markup.setChatId(chatId);
        markup.setMessageId(messageId);
        markup.setReplyMarkup(
                responseBody.getButtons() != null ? TelegramButtons.getInlineKeyboard(responseBody) : null);
        if (responseBody.isRemovable()) {
            markup.setRemovable(true);
        }
        return markup;
    }

    public CustomSendDocument buildSendDocument(ResponseBody responseBody){
        CustomSendDocument sendDocument = new CustomSendDocument();
        if(responseBody.getChatid() != null){
            sendDocument.setChatId(responseBody.getChatid());
        }else{
            sendDocument.setChatId(responseBody.getUserid());
        }

        if(responseBody.getFile() != null){
            sendDocument.setDocument(new InputFile(responseBody.getFile(), responseBody.getFile().getName()));
        }

        if(responseBody.getResponse() != null && !responseBody.getResponse().isEmpty()){
            sendDocument.setCaption(responseBody.getResponse());
        }

        if(responseBody.getButtons() != null){
            // Los documentos solo admiten Inline Keyboards (no Reply Keyboards)
            sendDocument.setReplyMarkup(TelegramButtons.getInlineKeyboard(responseBody));
        }

        return sendDocument;
    }   

    

}
