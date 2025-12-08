package dev.yonel.wireguardbot.core.commands.user.configuracion;

import java.util.List;
import java.util.Optional;

import dev.yonel.wireguardbot.common.context.UserSessionContext;
import dev.yonel.wireguardbot.common.dtos.PeerDto;
import dev.yonel.wireguardbot.common.dtos.UserDto;
import dev.yonel.wireguardbot.common.dtos.telegram.MessageBody;
import dev.yonel.wireguardbot.common.dtos.telegram.ResponseBody;
import dev.yonel.wireguardbot.common.services.database.UserService;
import dev.yonel.wireguardbot.message_manager.command.CommandBase;
import dev.yonel.wireguardbot.message_manager.command.interfaces.Command;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ObtenerConfiguracionCommand extends CommandBase implements Command{

    public static final String NAME = "/obtener_configuracion";
    public static final String[] ALIASES = {"/obtener_configuracion", "obtener configuracion", "obtener perfil"};

    @Autowired
    private UserService userService;

    @Override
    public List<ResponseBody> execute(MessageBody messageBody, UserSessionContext context) throws Throwable {
        initialize();

        Optional<UserDto> userOptional = userService.getUserByUserId(messageBody.getUserid());
        if (userOptional.isEmpty()) {
            createNewResponse(messageBody,
                    "❌ No se pudo encontrar tu información de usuario.");
            getCurrentResponse().setRemovable(true);
            return getResponses();
        }

        UserDto user = userOptional.get();

        if(user.getPeers().isEmpty()){
            createNewResponse(messageBody,
                    "❌ No tienes configuraciones creadas por favor crea "
                            +"una mediante el comando /crear_configuracion.");
            getCurrentResponse().setRemovable(true);
            return getResponses();
        }

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
