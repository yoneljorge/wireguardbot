
package dev.yonel.wireguardbot.message_manager.messages;

import dev.yonel.wireguardbot.common.utils.HTMLMessageBuilder;

public class StartMessages {

	public static String getMessageDefault() {
		HTMLMessageBuilder htmlBuilder = new HTMLMessageBuilder();
        
        htmlBuilder.addLine("Hola:");
        htmlBuilder.addBoldLine("¿En qué puedo ayudarte hoy? 😊");
        
        return htmlBuilder.build();
	}
}
