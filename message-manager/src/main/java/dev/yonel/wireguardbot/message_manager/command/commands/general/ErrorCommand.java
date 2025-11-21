package dev.yonel.wireguardbot.message_manager.command.commands.general;

import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import dev.yonel.wireguardbot.common.context.UserSessionContext;
import dev.yonel.wireguardbot.common.dtos.telegram.MessageBody;
import dev.yonel.wireguardbot.common.dtos.telegram.ResponseBody;
import dev.yonel.wireguardbot.message_manager.command.CommandBase;
import dev.yonel.wireguardbot.message_manager.command.interfaces.GeneralCommandInterface;
import dev.yonel.wireguardbot.message_manager.messages.ErrorMessage;
import lombok.extern.slf4j.Slf4j;

@Lazy
@Slf4j
@Component
public class ErrorCommand extends CommandBase implements GeneralCommandInterface {
    @Override
    public List<ResponseBody> execute(MessageBody message, UserSessionContext context) throws Throwable {
        initialize();
        createNewResponse(message, ErrorMessage.getMessage());

        return getResponses();
    }

    @Override
    public String getName() {
        return "error";
    }

    @Override
    public String[] getAliases() {
        return new String[] {
                "error"
        };
    }

}
