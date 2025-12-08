
package dev.yonel.wireguardbot.message_manager.messages;

import dev.yonel.wireguardbot.common.dtos.telegram.ResponseBody;
import dev.yonel.wireguardbot.common.enums.TypeParseMode;
import dev.yonel.wireguardbot.common.utils.HTMLMessageBuilder;

public class StartMessages {

	public static void getMessageDefault(ResponseBody responseBody) {
		HTMLMessageBuilder htmlBuilder = new HTMLMessageBuilder();
        htmlBuilder.add("Hola:");
        htmlBuilder.addBoldLine("¿En qué puedo ayudarte hoy? 😊");
        htmlBuilder.addBoldItalicLine("Escribe menu o has click aquí '/menu'");
        responseBody.setResponse(htmlBuilder.build());
        responseBody.setParseMode(TypeParseMode.HTML);
	}
}
