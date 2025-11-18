
package dev.yonel.wireguardbot.bot.components;

import org.springframework.stereotype.Component;

import dev.yonel.wireguardbot.common.dtos.telegram.ResponseBody;
import dev.yonel.wireguardbot.common.enums.TypeCustomKeyboardMarkup;
import dev.yonel.wireguardbot.common.utils.MarkdownMessageBuilder;
import dev.yonel.wireguardbot.bot.components.custom.CustomEditMessageReplyMarkup;
import dev.yonel.wireguardbot.bot.components.custom.CustomEditMessageText;
import dev.yonel.wireguardbot.bot.components.custom.CustomSendMessage;

@Component
public class TelegramMessageBuilder {

    public CustomSendMessage buildSendMessage(ResponseBody responseBody) {
        CustomSendMessage sendMessage = new CustomSendMessage();
        sendMessage.setChatId(responseBody.getChatid());

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
}
