
package dev.yonel.wireguardbot.bot.queue;

import org.telegram.telegrambots.meta.api.objects.Update;

import dev.yonel.wireguardbot.common.enums.TypeWebhookTelegramBot;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class IncomingMessageTelegram {

	private String userid;
	private int updateid;
	private Update updateMessage;
    private TypeWebhookTelegramBot bot;

	public IncomingMessageTelegram(Update update, TypeWebhookTelegramBot bot) {
		this.updateMessage = update;
        this.bot = bot;
		loadData();
	}

	private void loadData() {
		this.updateid = updateMessage.getUpdateId();
		if (updateMessage.hasCallbackQuery()) {
			this.userid = updateMessage.getCallbackQuery().getId();
		} else {
			this.userid = String.valueOf(updateMessage.getMessage().getChat().getId());
		}
	}
}
