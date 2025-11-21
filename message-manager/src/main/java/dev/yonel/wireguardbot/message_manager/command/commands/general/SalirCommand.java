package dev.yonel.wireguardbot.message_manager.command.commands.general;

import java.util.List;

import org.springframework.stereotype.Component;

import dev.yonel.wireguardbot.common.context.UserSessionContext;
import dev.yonel.wireguardbot.common.dtos.telegram.MessageBody;
import dev.yonel.wireguardbot.common.dtos.telegram.ResponseBody;
import dev.yonel.wireguardbot.message_manager.command.CommandBase;
import dev.yonel.wireguardbot.message_manager.command.interfaces.GeneralCommandInterface;

@Component
public class SalirCommand extends CommandBase implements GeneralCommandInterface {

    @Override
    public List<ResponseBody> execute(MessageBody body, UserSessionContext context) {
        initialize();

        if (!context.getBotSession(body.getTypeBot()).getActiveFlow().equals("")) {
            context.getBotSession(body.getTypeBot()).reset();
            createNewResponse(body, "🚫 Operación cancelada");
        } else {
            createNewResponse(body, "No hay operaciones actualmente.");
        }

        return getResponses();
    }

    @Override
    public String getName() {
        return "salir";
    }

    @Override
    public String[] getAliases() {
        return new String[] {
                "/salir",
                "salir",
                "exit",
                "no es eso lo que te pedi",
                "yo digo otra cosa"
        };
    }

}
