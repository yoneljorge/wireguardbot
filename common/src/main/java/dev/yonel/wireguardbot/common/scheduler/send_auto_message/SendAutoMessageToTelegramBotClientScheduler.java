
package dev.yonel.wireguardbot.common.scheduler.send_auto_message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import dev.yonel.wireguardbot.common.properties.SchedulerAutoSendMessageProperties;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@EnableScheduling
@ConditionalOnProperty(name = "scheduler.autosendmessage.active", havingValue = "true")
public class SendAutoMessageToTelegramBotClientScheduler {

	@Autowired
	private SendToTelegramBotClientSchedulerService sendToWebhookService;

	private int TIME;
	private static int count = 0;


	public SendAutoMessageToTelegramBotClientScheduler(SchedulerAutoSendMessageProperties properties) {
		this.TIME = properties.getTime();
	}

	

	@Scheduled(fixedRate = 1000) // Cada 1 segundos (en milisegundos)
	public void sendMessageToBot() {
		count++;

		if (count == TIME) {
			run();
			count = 0;
		}
	}

	private void run() {
		// Enviando mensaje automatizado
		sendToWebhookService.triggerWebhook();
		log.info("Enviando mensage automatico.");
	}

	public static void resetCount() {
		count = 0;
	}
}
