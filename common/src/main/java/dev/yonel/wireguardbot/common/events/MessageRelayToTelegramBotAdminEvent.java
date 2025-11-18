package dev.yonel.wireguardbot.common.events;

import java.util.EventObject;
import java.util.List;

import dev.yonel.wireguardbot.common.dtos.telegram.ResponseBody;

public class MessageRelayToTelegramBotAdminEvent extends EventObject {
    private final List<ResponseBody> responses;

    public MessageRelayToTelegramBotAdminEvent(Object source, List<ResponseBody> responses) {
        super(source);
        this.responses = responses;
    }

    public List<ResponseBody> getResponses() {
        return responses;
    }
}
