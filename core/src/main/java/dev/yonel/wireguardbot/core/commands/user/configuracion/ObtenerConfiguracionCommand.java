package dev.yonel.wireguardbot.core.commands.user.configuracion;

import java.util.List;

import dev.yonel.wireguardbot.common.context.UserSessionContext;
import dev.yonel.wireguardbot.common.dtos.telegram.MessageBody;
import dev.yonel.wireguardbot.common.dtos.telegram.ResponseBody;
import dev.yonel.wireguardbot.message_manager.command.CommandBase;
import dev.yonel.wireguardbot.message_manager.command.interfaces.Command;

public class ObtenerConfiguracionCommand extends CommandBase implements Command{

    public static final String NAME = "/obtener_configuracion";
    public static final String[] ALIASES = {"/obtener_configuracion", "obtener configuracion", "obtener perfil"};

    @Override
    public List<ResponseBody> execute(MessageBody messageBody, UserSessionContext context) throws Throwable {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'execute'");
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String[] getAliases() {
        return ALIASES;
    }

}
