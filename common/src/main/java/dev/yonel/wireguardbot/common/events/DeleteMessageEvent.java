package dev.yonel.wireguardbot.common.events;

import java.util.EventObject;

import lombok.Getter;

@Getter
public class DeleteMessageEvent extends EventObject {
    private final String chatId;
    private final int messageId;
    
    public DeleteMessageEvent(Object source, String chatId, int messageId){
        super(source);
        this.chatId = chatId;
        this.messageId = messageId;
    }
}
