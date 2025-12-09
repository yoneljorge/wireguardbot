package dev.yonel.wireguardbot.core.commands.user.configuracion;

import java.util.*;

import dev.yonel.wireguardbot.common.context.SessionKey;
import dev.yonel.wireguardbot.common.context.UserSessionContext;
import dev.yonel.wireguardbot.common.dtos.PeerDto;
import dev.yonel.wireguardbot.common.dtos.UserDto;
import dev.yonel.wireguardbot.common.dtos.telegram.Button;
import dev.yonel.wireguardbot.common.dtos.telegram.MessageBody;
import dev.yonel.wireguardbot.common.dtos.telegram.ResponseBody;
import dev.yonel.wireguardbot.common.dtos.wireguard.WireGuardPeer;
import dev.yonel.wireguardbot.common.dtos.wireguard.WireGuardPeerResponse;
import dev.yonel.wireguardbot.common.enums.TypeCustomButton;
import dev.yonel.wireguardbot.common.enums.TypeParseMode;
import dev.yonel.wireguardbot.common.services.database.UserService;
import dev.yonel.wireguardbot.common.utils.HTMLMessageBuilder;
import dev.yonel.wireguardbot.core.client.WireGuardAgentClient;
import dev.yonel.wireguardbot.core.commands.user.configuracion.utils.ConfiguracionCommandUtils;
import dev.yonel.wireguardbot.core.db.repositories.PeerRepository;
import dev.yonel.wireguardbot.core.properties.WireguardServerProperties;
import dev.yonel.wireguardbot.message_manager.command.CommandBase;
import dev.yonel.wireguardbot.message_manager.command.interfaces.Command;
import dev.yonel.wireguardbot.message_manager.command.interfaces.UserCommandInterface;
import dev.yonel.wireguardbot.message_manager.messages.ErrorMessage;
import dev.yonel.wireguardbot.message_manager.messages.NoEntendiMessages;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EliminarConfiguracionCommand extends CommandBase implements UserCommandInterface {

    public static final String NAME = "/eliminar_configuracion";
    public static final String[] ALIASES = {"/eliminar_configuracion", "eliminar configuracion", "eliminar perfil"};

    @Autowired
    private ConfiguracionCommandUtils configuracionCommandUtils;
    @Autowired
    private UserService userService;
    @Autowired
    private PeerRepository peerRepository;
    @Autowired
    private WireGuardAgentClient agentClient;
    @Autowired
    private WireguardServerProperties wireguardServerProperties;

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String[] getAliases() {
        return ALIASES;
    }

    @Override
    public List<ResponseBody> execute(MessageBody messageBody, UserSessionContext context) throws Throwable {
        initialize();

        try {
            if (configuracionCommandUtils.isNotActiveAgent()) {
                createNewResponse(messageBody,
                        "⚠️ El servicio de WireGuard no está disponible en este momento. Por favor, intenta más tarde.");
                getCurrentResponse().setRemovable(true);
                return getResponses();
            }

            Optional<UserDto> userOptional = userService.getUserByUserId(messageBody.getUserid());
            if (userOptional.isEmpty()) {
                createNewResponse(messageBody,
                        "❌ No se pudo encontrar tu información de usuario.");
                getCurrentResponse().setRemovable(true);
                return getResponses();
            }

            UserDto user = userOptional.get();

            switch (context.getBotSession(messageBody.getTypeBot()).getStep()){
                case 0 -> {
                    return step0(messageBody, context, user);
                }

                case 1-> {
                    return step1(messageBody, context, user);
                }

                default -> {
                    createNewResponse(messageBody,
                            NoEntendiMessages.noEntendi());
                    return getResponses();
                }
            }

        }catch (Exception e){
            log.error("Error  ejecutando comando eliminar: {}", e.getMessage());
            context.getBotSession(messageBody.getTypeBot()).reset();
            e.printStackTrace();
            createNewResponse(messageBody,
                    ErrorMessage.getMessage());
            return getResponses();
        }
    }

    private List<ResponseBody> step0(MessageBody messageBody, UserSessionContext context, UserDto user){
        if(user.getPeers().isEmpty()){
            createNewResponse(messageBody,
                    "❌ No tienes configuraciones creadas, puedes crear "
                            + "una mediante el comando /crear_configuracion.");
            getCurrentResponse().setRemovable(true);
            return getResponses();
        }

        createNewResponse(messageBody,
                "Presiona eliminar para eliminar la configuración: ");
        int cantPeers = 0;
        Map<Long, String> peersPublicKey = new HashMap<>();
        for(PeerDto peer : user.getPeers()){
            peersPublicKey.put(peer.getId(), peer.getPublicKey());
            createNewResponse(messageBody);
            buildResponsePeer(getCurrentResponse(), peer);
            cantPeers ++;
        }
        /*
        Guardamos la publicKey de cada peer junto con su id para obtener estos datos en el paso 1.
         */
        context.getBotSession(messageBody.getTypeBot()).putData(SessionKey.ELIMINAR_CONFIGURACION_COMMAND_MAP_PEERS_PUBLIC_KEY, peersPublicKey);
        /*
         * Si la cantidad de peer es mayor que uno entonces agregamos un botón de
         * terminar para que el usuario vaya eliminando de uno en uno los botones
         * y cuando termine entonces que presione el botón terminar.
         */
        if(cantPeers > 1){
            context.getBotSession(messageBody.getTypeBot()).putData(SessionKey.ELIMINAR_CONFIGURACION_COMMAND_ESPERAR, true);
            createNewResponse(messageBody);
            getCurrentResponse().setButtons(List.of(
                    Button.builder()
                            .callbackData("terminar")
                            .text("Terminar")
                            .typeButton(TypeCustomButton.CALLBACKDATA)
                            .build()
            ));
        }else{
            context.getBotSession(messageBody.getTypeBot()).putData(SessionKey.ELIMINAR_CONFIGURACION_COMMAND_ESPERAR, false);
        }

        context.getBotSession(messageBody.getTypeBot()).setActiveFlow(NAME);
        context.getBotSession(messageBody.getTypeBot()).nextStep();
        return  getResponses();
    }

    private List<ResponseBody> step1(MessageBody messageBody, UserSessionContext context, UserDto user){
        if(messageBody.getMessage().contains("eliminar")){
            try{
                /*
                Obtenemos id del PeerEntity el cual viene en el mensaje.
                Formato del mensaje esperado [eliminar_id]
                */
                long idPeer = Long.parseLong(messageBody.getMessage().substring(messageBody.getMessage().indexOf('_') + 1));
                Map<Long, String> publicKey = ( Map<Long, String> )context.getBotSession(
                        messageBody.getTypeBot()).getData(SessionKey.ELIMINAR_CONFIGURACION_COMMAND_MAP_PEERS_PUBLIC_KEY);

                WireGuardPeer peer = WireGuardPeer.builder()
                        .publicKey(publicKey.get(idPeer))
                        .build();
                WireGuardPeerResponse peerResponse = agentClient.removePeer(
                        wireguardServerProperties.getName(),
                        peer).getBody();

                if(peerResponse == null || !peerResponse.isSuccess()){
                    createNewResponse(messageBody,
                            "❌ No se pudo eliminar la configuración del servidor. Intente más tarde o contacte con Soporte.");
                    context.getBotSession(messageBody.getTypeBot()).reset();
                    log.warn("Error accediendo al microservicio Agent -> Puede estar caído.");
                    return getResponses();
                }
                peerRepository.deleteById(idPeer);

            }catch (IllegalFormatException e){
                log.error("Error al parsear un String a Long: {}", e.getMessage());
                createNewResponse(messageBody,
                        ErrorMessage.getMessage());
                context.getBotSession(messageBody.getTypeBot()).reset();
                e.printStackTrace();
                return getResponses();
            }catch (Exception e){
                log.error("Error inesperado al parsear un String a Long: {}", e.getMessage());
                createNewResponse(messageBody,
                        ErrorMessage.getMessage());
                context.getBotSession(messageBody.getTypeBot()).reset();
                e.printStackTrace();
                return getResponses();
            }

        }

        /*
        Cuando se presione el botón terminar entonces se termina la espera.
         */
        if(messageBody.getMessage().equalsIgnoreCase("terminar")){
            context.getBotSession(messageBody.getTypeBot()).removeLastActiveFlow();
            context.getBotSession(messageBody.getTypeBot()).cleanData();
            context.getBotSession(messageBody.getTypeBot()).resetStep();

            // Revisamos si se ha eliminado con éxito los peers seleccionados
            createNewResponse(messageBody,
                    "Se ha terminado de eliminar las configuraciones seleccionadas.");
        }

        /*
        En caso de que no se espere al botón terminar, entonces se termina la espera y se cierra el contexto.
         */
        if(! (boolean) context.getBotSession(messageBody.getTypeBot()).getData(SessionKey.ELIMINAR_CONFIGURACION_COMMAND_ESPERAR)){
            context.getBotSession(messageBody.getTypeBot()).cleanData();
            context.getBotSession(messageBody.getTypeBot()).removeLastActiveFlow();
            context.getBotSession(messageBody.getTypeBot()).resetStep();

            createNewResponse(messageBody,
                    "Se ha terminado de eliminar la configuración.");
            return getResponses();
        }else{
            return null;
        }

    }

    private void buildResponsePeer(ResponseBody responseBody, PeerDto peerDto){
        HTMLMessageBuilder htmlBuilder = new HTMLMessageBuilder();
        htmlBuilder.addBold("IP: ");
        htmlBuilder.addItalicLine(peerDto.getIpDto().getIpString());
        htmlBuilder.addBold("Creado: ");
        htmlBuilder.addItalicLine(peerDto.getCreatedAt().toString());
        htmlBuilder.addBold("Vence: ");
        htmlBuilder.addItalicLine(peerDto.getPaidUpTo().toString());
        responseBody.setResponse(htmlBuilder.build());
        responseBody.setParseMode(TypeParseMode.HTML);
        responseBody.setButtons(List.of(
                Button.builder()
                        .text("Eliminar")
                        .callbackData("eliminar_" + peerDto.getId())
                        .typeButton(TypeCustomButton.CALLBACKDATA)
                        .build()
        ));
    }


}
