
package dev.yonel.wireguardbot.common.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Configuration
@ConfigurationProperties("scheduler.autosendmessage")
public class SchedulerAutoSendMessageProperties {
	//FIXME cabiar el nombre del field en funcion de lo que debe ejecutar
	private String appUrl;
	private boolean active;
	private int chatid;
	private int time;
	private String message;
}
