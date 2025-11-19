package dev.yonel.wireguardbot.common.events;

import java.util.EventObject;
import java.util.List;

import dev.yonel.wireguardbot.common.dtos.telegram.ResponseBody;
import lombok.Getter;

@Getter
public class MessageRelayToTelegramBotClientEvent extends EventObject {
    private List<ResponseBody> responses;

	public MessageRelayToTelegramBotClientEvent(Object source, List<ResponseBody> responses) {
		super(source);
		this.responses = responses;
	}
}
