
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

	public List<CustomSendMessage> receivedMessageFromPrivate(Object updateObject,
			TypeWebhookTelegramBot bot) throws Throwable {
		try {
			return messageProcessor.processTextMessage((Update) updateObject, bot);
		} catch (Exception e) {
			return null;
		}
	}

	public List<CustomSendMessage> receivedImageMessageFromPrivate(Object updateObject,
			String fileUrl, TypeWebhookTelegramBot bot) throws Throwable {
		try {
			return messageProcessor.processImageMessage((Update) updateObject, fileUrl, bot);
		} catch (Exception e) {
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

	public List<CustomSendMessage> onReceivedEventFromMessageRelay(List<ResponseBody> responses) {
		try {
			return messageProcessor.processResponseFromMessageRelay(responses);
		} catch (Exception e) {
			return null;
		}
	}

	public List<CustomSendMessage> receivedFromButtons(Object updateObject, TypeWebhookTelegramBot bot) throws Throwable {
		try {
			return buttonProcessor.processButtonClick((Update) updateObject, bot);
		} catch (Exception e) {
			return null;
		}
	}

	public List<CustomEditMessageText> editMessageText(Object updateObject, TypeWebhookTelegramBot bot) throws Throwable {
		try {
			return buttonProcessor.processEditMessageText((Update) updateObject, bot);
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