package dev.yonel.wireguardbot.message_manager.command;


import java.util.List;

import dev.yonel.wireguardbot.common.context.UserSessionContext;
import dev.yonel.wireguardbot.common.dtos.telegram.MessageBody;
import dev.yonel.wireguardbot.common.dtos.telegram.ResponseBody;


public interface Command {

    List<ResponseBody> execute(MessageBody messageBody, UserSessionContext context) throws Throwable;

    String getName();

    String[] getAliases(); // Frases con las que puede coincidir.

}
