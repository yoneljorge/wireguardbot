package dev.yonel.wireguardbot.common.events;

import java.util.EventObject;

import lombok.Getter;

@Getter
public class MessageRelayToDeleteMessageEvent extends EventObject {
    private long chatId;
    private int messageId;
    
    public MessageRelayToDeleteMessageEvent(Object source, long chatId, int messageId){
        super(source);
        this.chatId = chatId;
        this.messageId = messageId;
    }
}
