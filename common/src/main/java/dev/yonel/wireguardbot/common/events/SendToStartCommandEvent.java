package dev.yonel.wireguardbot.common.events;

import org.springframework.context.ApplicationEvent;

import dev.yonel.wireguardbot.common.dtos.telegram.MessageBody;
import lombok.Getter;

@Getter
public class SendToStartCommandEvent extends ApplicationEvent {

    private final MessageBody messageBody;

    private final Object context;

    public SendToStartCommandEvent(Object source, MessageBody messageBody, Object context) {
        super(source);
        this.messageBody = messageBody;
        this.context = context;
    }
}
