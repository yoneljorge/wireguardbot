package dev.yonel.wireguardbot.core.commands.user.configuracion;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import dev.yonel.wireguardbot.common.dtos.IpDto;
import dev.yonel.wireguardbot.common.dtos.PeerDto;
import dev.yonel.wireguardbot.common.dtos.UserDto;
import dev.yonel.wireguardbot.common.dtos.wireguard.WireGuardKeyPair;
import dev.yonel.wireguardbot.common.dtos.wireguard.WireGuardPeer;
import dev.yonel.wireguardbot.common.dtos.wireguard.WireGuardPeerResponse;
import dev.yonel.wireguardbot.common.events.MessageRelayToTelegramBotClientEvent;
import dev.yonel.wireguardbot.common.services.database.IpService;
import dev.yonel.wireguardbot.common.services.database.UserService;
import dev.yonel.wireguardbot.core.client.WireGuardAgentClient;
import dev.yonel.wireguardbot.core.commands.user.configuracion.utils.BuildConfig;
import dev.yonel.wireguardbot.core.properties.WireguardServerProperties;
import dev.yonel.wireguardbot.message_manager.command.interfaces.UserCommandInterface;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import dev.yonel.wireguardbot.common.context.UserSessionContext;
import dev.yonel.wireguardbot.common.dtos.telegram.MessageBody;
import dev.yonel.wireguardbot.common.dtos.telegram.ResponseBody;
import dev.yonel.wireguardbot.message_manager.command.CommandBase;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CrearConfiguracionCommand extends CommandBase implements UserCommandInterface {

    public static final String NAME = "/crear_configuracion";
    public static final String[] ALIASES = {"/crear_configuracion", "crear configuracion", "crear perfil"};

    @Autowired
    private DiscoveryClient discoveryClient;
    @Autowired
    private UserService userService;
    @Autowired
    private IpService ipService;
    @Autowired
    private WireGuardAgentClient agentClient;
    @Autowired
    private WireguardServerProperties wireguardServerProperties;
    @Autowired
    private ApplicationEventPublisher publisher;

    @Override
    public List<ResponseBody> execute(MessageBody messageBody, UserSessionContext context){
        // Inicializamos para borrar posibles respuestas remanentes y para inicializar en ArrayList
        initialize();

        try{
            if (!isActiveAgent()) {
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
            PeerDto peerDto = new PeerDto();

            if (user.getPeers().isEmpty()) {
                if (!user.isFreePlanEnded()) {
                    /*
                     * Si el usuario no ha utilizado el plan Free entonces se le permite utilizarlo
                     * y se activa la fecha.
                     */
                    notificarUsuario(user, "⏳ Creando configuración...", true);

                    user.setActivedFreePlan(LocalDate.now());
                    peerDto.setActive(true);

                    /*
                     * Obtenemos una nueva direccion ip.
                     */
                    String newIpAddress = ipService.getNewIp();
                    if (newIpAddress == null) {
                        createNewResponse(messageBody,
                                "❌ No se pudo asignar una dirección IP. Por favor, contacta con el soporte.");
                        getCurrentResponse().setRemovable(true);
                        return getResponses();
                    }

                    /*
                     * Obtenemos el par de claves.
                     */
                    WireGuardKeyPair keyPair = agentClient.generateKeyPair().getBody();
                    if (keyPair == null) {
                        createNewResponse(messageBody,
                                "❌ No se pudieron generar las claves de seguridad. Por favor, intenta más tarde.");
                        getCurrentResponse().setRemovable(true);
                        return getResponses();
                    }

                    /*
                     * Terminamos de crear el peer del usuario y le enviamos la configuración.
                     */
                    peerDto.setPrivateKey(keyPair.getPrivateKey());
                    peerDto.setPublicKey(keyPair.getPublicKey());
                    peerDto.setCreatedAt(LocalDate.now());
                    IpDto ip = new IpDto();
                    ip.setIpString(newIpAddress);
                    peerDto.setIpDto(ip);
                    /*
                     * Como el plan Free es de 7 días se pone la fecha
                     * de vencimiento a los 7 días.
                     */
                    peerDto.setPaidUpTo(LocalDate.now().plusDays(7));
                    user.setPeer(peerDto);

                    /*
                     * Actualizamos el usuario en la base de datos con la nueva información.
                     */
                    Optional<UserDto> updateUser = userService.updateUser(user);
                    /*
                     * Si el usuario se actualizó correctamente entonces procedemos a generar el
                     * archivo de configuración.
                     */
                    if (updateUser != null && updateUser.isPresent()) {
                        try {
                            File configFile = BuildConfig.buildConfig(wireguardServerProperties, user, peerDto);

                            /*
                             * Creamos el nuevo peer que se va a agregar al servidor WireGuard.
                             */
                            WireGuardPeer newPeer = WireGuardPeer.builder()
                                    .publicKey(keyPair.getPublicKey())
                                    .allowedIp(newIpAddress)
                                    .build();

                            /*
                             * Agregamos el nuevo peer al servidor y esperamos la respuesta.
                             */
                            WireGuardPeerResponse peerResponse = agentClient
                                    .addPeer(wireguardServerProperties.getName(), newPeer).getBody();

                            if (peerResponse == null || !peerResponse.isSuccess()) {
                                createNewResponse(messageBody,
                                        "❌ No se pudo agregar la configuración al servidor. Por favor, intenta más tarde." );
                                getCurrentResponse().setRemovable(true);
                                return getResponses();
                            }

                            // Crear mensaje con texto
                            createNewResponse(messageBody,
                                    "✅ ¡Configuración creada exitosamente!\n"
                                            +"\n📁 Aquí tienes tu archivo de configuración de WireGuard.\n"
                                            +"\n⏰ Tu plan gratuito expira el: " + peerDto.getPaidUpTo());
                            createNewResponse(messageBody);
                            getCurrentResponse().setFile(configFile);
                            return getResponses();

                        } catch (IOException e) {
                            log.error("Error generando archivo de configuración para usuario {}: {}", messageBody.getUserid(), e.getMessage());

                            createNewResponse(messageBody,
                                    "❌ Error al generar el archivo de configuración. Por favor, contacta con el soporte.");
                            // FIXME: aquí hay que notificar a los administradores del problema.
                            return getResponses();
                        }
                    } else {
                        createNewResponse(messageBody,
                                "❌ No se pudo actualizar tu información. Por favor, intenta más tarde.");

                        return getResponses();
                    }
                } else {
                    /*
                     * Si el usuario ya utilizó su plan Free entonces tiene que pagar.
                     * Se le envia un mensaje diciendole que para activar el servicio
                     * necesita realizar el pago de la suscripción.
                     */
                    createNewResponse(messageBody,
                            "💳 Tu plan gratuito ha expirado. Para continuar usando el servicio, necesitas renovar tu suscripción.");
                    getCurrentResponse().setRemovable(true);
                    return getResponses();
                }
            } else {
                peerDto.setActive(false);
                createNewResponse(messageBody,
                        "💳 Ya tienes una configuración activa. Para crear una nueva, necesitas renovar tu suscripción.");
                getCurrentResponse().setRemovable(true);
                return getResponses();
            }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String[] getAliases() {
        return new String[]{"/crear_configuracion", "crear configuracion", "crear perfil"};
    }

    /**
     * Verifica que exista una instancia de Agent.
     *
     * @return <code>true</code> en caso de que exista una.
     */
    private boolean isActiveAgent() {
        final String agentServiceName = "wireguardbot-agent";
        if (discoveryClient != null && !discoveryClient.getInstances(agentServiceName).isEmpty()) {
            return true;
        } else {
            return false;
        }
    }


    private void notificarUsuario(UserDto user, String message, boolean removable) {
        /*
         * Enviamos una notificacion mediante el ClientBot para que el usuario.
         */
        publisher.publishEvent(new MessageRelayToTelegramBotClientEvent(this,
                List.of(ResponseBody.builder()
                        .userid(user.getUserId())
                        .removable(removable)
                        .response(message)
                        .build())));
    }

}
