package dev.yonel.wireguardbot.common.services.message_manager;

import java.util.List;

import dev.yonel.wireguardbot.common.dtos.telegram.MessageBody;
import dev.yonel.wireguardbot.common.dtos.telegram.ResponseBody;

public interface MessageRelayService {

	List<ResponseBody> handleMessage(MessageBody messageBody) throws Throwable;
}
