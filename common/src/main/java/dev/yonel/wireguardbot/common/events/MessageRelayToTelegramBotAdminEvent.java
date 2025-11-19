package dev.yonel.wireguardbot.common.events;

import java.util.EventObject;
import java.util.List;

import dev.yonel.wireguardbot.common.dtos.telegram.ResponseBody;
import lombok.Getter;

@Getter
public class MessageRelayToTelegramBotAdminEvent extends EventObject {
    private final List<ResponseBody> responses;

    public MessageRelayToTelegramBotAdminEvent(Object source, List<ResponseBody> responses) {
        super(source);
        this.responses = responses;
    }
}
