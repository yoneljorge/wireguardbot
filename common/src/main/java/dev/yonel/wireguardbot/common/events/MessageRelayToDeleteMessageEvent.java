package dev.yonel.wireguardbot.common.events;

import java.util.EventObject;

public class MessageRelayToDeleteMessageEvent extends EventObject {
    private final Long chatId;
    private final int messageId;

    public MessageRelayToDeleteMessageEvent(Object source, Long chatId, int messageId) {
        super(source);
        this.chatId = chatId;
        this.messageId = messageId;
    }

    public Long getChatId() {
        return chatId;
    }

    public int getMessageId() {
        return messageId;
    }
}
