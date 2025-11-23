package dev.yonel.wireguardbot.core.commands.user.configuracion;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import dev.yonel.wireguardbot.common.context.UserSessionContext;
import dev.yonel.wireguardbot.common.dtos.telegram.MessageBody;
import dev.yonel.wireguardbot.common.dtos.telegram.ResponseBody;
import dev.yonel.wireguardbot.core.services.WireguardService;
import dev.yonel.wireguardbot.message_manager.command.CommandBase;
import dev.yonel.wireguardbot.message_manager.command.interfaces.Command;

public class CrearConfiguracionCommand extends CommandBase implements Command{

    public final static String NAME = "crear_configuracion_command";

    @Autowired
    private WireguardService wireguardService;

    @Override
    public List<ResponseBody> execute(MessageBody messageBody, UserSessionContext context) throws Throwable {
        
        return wireguardService.crearConfiguracion(messageBody.getUserid());
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String[] getAliases() {
        return new String[]{"crear configuracion", "crear", "crear_configuracion"};
    }
    
}
