
package dev.yonel.wireguardbot.bot.components;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import dev.yonel.wireguardbot.common.dtos.telegram.ResponseBody;
import dev.yonel.wireguardbot.common.enums.TypeWebhookTelegramBot;
import dev.yonel.wireguardbot.bot.components.custom.CustomEditMessageReplyMarkup;
import dev.yonel.wireguardbot.bot.components.custom.CustomEditMessageText;
import dev.yonel.wireguardbot.bot.components.custom.CustomSendMessage;

@Lazy
@Component
public class TelegramPlatform {

	@Autowired
	private TelegramMessageProcessor messageProcessor;

	@Autowired
	private TelegramButtonProcessor buttonProcessor;

	@Autowired
	private TelegramUserManager userManager;

	public List<Object> receivedRequestMessageFromPrivate(
            TypeWebhookTelegramBot bot,
            Object updateObject,
			String fileUrl) throws Throwable {
		try {
			return messageProcessor.processRequestMessage(bot, (Update) updateObject, fileUrl);
		} catch (Exception e) {
			e.printStackTrace();
            return null;
		}
	}


	public void receivedMessageFromGroup() {

	}

	public void onNewUserJoinOnGroup(List<User> newUsers, TypeWebhookTelegramBot bot) {
		userManager.handleNewGroupUsers(newUsers, bot);
	}

    /*FIXME: Cuando un usuario sale del grupo */
	public void onUserLeftOnGroup(User user) {

	}

	public List<Object> onReceivedEventFromMessageRelay(List<ResponseBody> responses) {
		try {
			return messageProcessor.processResponses(responses);
		} catch (Exception e) {
			return null;
		}
	}


	public List<CustomEditMessageText> editMessageText(TypeWebhookTelegramBot bot, Object updateObject, String fileUrl) throws Throwable {
		try {
			return buttonProcessor.processEditMessageText(bot, (Update) updateObject, fileUrl);
		} catch (Exception e) {
			return null;
		}
	}

	public List<CustomEditMessageReplyMarkup> editButtons(Object updateObject, Object responseBody) {
		try {
			return buttonProcessor.processEditButtons((Update) updateObject, (ResponseBody) responseBody);
		} catch (Exception e) {
			return null;
		}
	}

	public List<CustomEditMessageReplyMarkup> removeButtons(Object updateObject, Object responseBody) {
		try {
			return buttonProcessor.processRemoveButtons((Update) updateObject, (ResponseBody) responseBody);
		} catch (Exception e) {
			return null;
		}
	}
}