package dev.yonel.wireguardbot.core.commands.user.configuracion;

import java.util.List;
import java.util.Optional;

import dev.yonel.wireguardbot.common.context.UserSessionContext;
import dev.yonel.wireguardbot.common.dtos.PeerDto;
import dev.yonel.wireguardbot.common.dtos.UserDto;
import dev.yonel.wireguardbot.common.dtos.telegram.MessageBody;
import dev.yonel.wireguardbot.common.dtos.telegram.ResponseBody;
import dev.yonel.wireguardbot.common.enums.TypeParseMode;
import dev.yonel.wireguardbot.common.services.database.UserService;
import dev.yonel.wireguardbot.common.utils.HTMLMessageBuilder;
import dev.yonel.wireguardbot.core.commands.user.configuracion.utils.BuildConfig;
import dev.yonel.wireguardbot.core.properties.WireguardServerProperties;
import dev.yonel.wireguardbot.message_manager.command.CommandBase;
import dev.yonel.wireguardbot.message_manager.command.interfaces.UserCommandInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ObtenerConfiguracionCommand extends CommandBase implements UserCommandInterface {

    public static final String NAME = "/obtener_configuracion";
    public static final String[] ALIASES = {"/obtener_configuracion", "obtener configuracion", "obtener perfil"};

    @Autowired
    private UserService userService;
    @Autowired
    private WireguardServerProperties wireguardServerProperties;

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
                            + "una mediante el comando /crear_configuracion.");
            getCurrentResponse().setRemovable(true);
            return getResponses();
        }

        for(PeerDto peerDto : user.getPeers()){
            createNewResponse(messageBody);
            buildResponsePeer(getCurrentResponse(), peerDto);
            createNewResponse(messageBody);
            getCurrentResponse().setFile(BuildConfig.buildConfig(
                    wireguardServerProperties,
                    user,
                    peerDto));
        }
        return getResponses();
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String[] getAliases() {
        return ALIASES;
    }

    private void buildResponsePeer(ResponseBody responseBody, PeerDto peerDto){
        HTMLMessageBuilder htmlBuilder = new HTMLMessageBuilder();
        htmlBuilder.addBold("Peer creado: ");
        htmlBuilder.addLine(peerDto.getCreatedAt().toString());
        htmlBuilder.addBold("Vence: ");
        htmlBuilder.addLine(peerDto.getPaidUpTo().toString());
        responseBody.setResponse(htmlBuilder.build());
        responseBody.setParseMode(TypeParseMode.HTML);
    }
}
