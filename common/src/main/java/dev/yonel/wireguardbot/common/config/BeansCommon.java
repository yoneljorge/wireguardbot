package dev.yonel.wireguardbot.common.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import dev.yonel.wireguardbot.common.properties.SchedulerAutoSendMessageProperties;
import dev.yonel.wireguardbot.common.scheduler.send_auto_message.SendAutoMessageToTelegramBotClientScheduler;
import dev.yonel.wireguardbot.common.scheduler.send_auto_message.SendToTelegramBotClientSchedulerService;

@Configuration
public class BeansCommon {

    @Autowired
	private SchedulerAutoSendMessageProperties properties;

	@Bean
	public RestTemplate restTemplate(){
		return new RestTemplate();
	}
	
	@Bean
	public SendAutoMessageToTelegramBotClientScheduler sendAutoMessageToTelegramBotClientScheduler(){
		return new SendAutoMessageToTelegramBotClientScheduler(properties);
	}

	@Bean
	public SendToTelegramBotClientSchedulerService sendToTelegramBotClientSchedulerService(){
		return new SendToTelegramBotClientSchedulerService(properties);
	}
}
