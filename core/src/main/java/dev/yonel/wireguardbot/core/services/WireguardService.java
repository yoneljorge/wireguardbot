package dev.yonel.wireguardbot.core.services;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import dev.yonel.wireguardbot.common.dtos.IpDto;
import dev.yonel.wireguardbot.common.dtos.PeerDto;
import dev.yonel.wireguardbot.common.dtos.UserDto;
import dev.yonel.wireguardbot.common.dtos.telegram.ResponseBody;
import dev.yonel.wireguardbot.common.dtos.wireguard.WireGuardKeyPair;
import dev.yonel.wireguardbot.common.dtos.wireguard.WireGuardPeer;
import dev.yonel.wireguardbot.common.dtos.wireguard.WireGuardPeerResponse;
import dev.yonel.wireguardbot.common.events.MessageRelayToTelegramBotClientEvent;
import dev.yonel.wireguardbot.common.services.database.IpService;
import dev.yonel.wireguardbot.common.services.database.UserService;
import dev.yonel.wireguardbot.core.client.WireGuardAgentClient;
import dev.yonel.wireguardbot.core.properties.WireguardServerProperties;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class WireguardService {

    private final DiscoveryClient discoveryClient;
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

    public WireguardService(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }

    public List<ResponseBody> crearConfiguracion(Long userId) {
        List<ResponseBody> responses = new ArrayList<>();
        
        if (!isActiveAgent()) {
            responses.add(ResponseBody.builder()
                    .userid(userId)
                    .response("⚠️ El servicio de WireGuard no está disponible en este momento. Por favor, intenta más tarde.")
                    .removable(true)
                    .build());
            return responses;
        }

        Optional<UserDto> userOptional = userService.getUserByUserId(userId);
        if (!userOptional.isPresent()) {
            responses.add(ResponseBody.builder()
                    .userid(userId)
                    .response("❌ No se pudo encontrar tu información de usuario.")
                    .removable(true)
                    .build());
            return responses;
        }

        UserDto user = userOptional.get();
        PeerDto peerDto = new PeerDto();

        if (user.getPeers().size() == 0) {
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
                    responses.clear();
                    responses.add(ResponseBody.builder()
                            .userid(userId)
                            .response("❌ No se pudo asignar una dirección IP. Por favor, contacta con el soporte.")
                            .removable(true)
                            .build());
                    return responses;
                }

                /*
                 * Obtenemos el par de claves.
                 */
                WireGuardKeyPair keyPair = agentClient.generateKeyPair().getBody();
                if (keyPair == null) {
                    responses.clear();
                    responses.add(ResponseBody.builder()
                            .userid(userId)
                            .response("❌ No se pudieron generar las claves de seguridad. Por favor, intenta más tarde.")
                            .removable(true)
                            .build());
                    return responses;
                }

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
                    responses.clear();
                    responses.add(ResponseBody.builder()
                            .userid(userId)
                            .response("❌ No se pudo agregar la configuración al servidor. Por favor, intenta más tarde.")
                            .removable(true)
                            .build());
                    return responses;
                }

                /*
                 * Terminamos de crear el peer del usuario y le enviamos la configuración.
                 */
                peerDto.setPrivateKey(keyPair.getPrivateKey());
                peerDto.setPublicKey(keyPair.getPublicKey());
                peerDto.setCreatedAt(LocalDate.now());
                IpDto ip = new IpDto();
                ip.setIpString(newIpAddress);
                peerDto.setIp(ip);
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
                        File configFile = buildConfig(wireguardServerProperties, user, peerDto);
                        
                        // Limpiar la respuesta anterior de "Creando configuración..."
                        responses.clear();
                        
                        // Crear la respuesta con el archivo de configuración
                        responses.add(ResponseBody.builder()
                                .userid(userId)
                                .file(configFile)
                                .response("✅ ¡Configuración creada exitosamente!\n\n📁 Aquí tienes tu archivo de configuración de WireGuard.\n\n⏰ Tu plan gratuito expira el: " + peerDto.getPaidUpTo())
                                .removable(false)
                                .build());
                        
                        return responses;
                    } catch (IOException e) {
                        log.error("Error generando archivo de configuración para usuario {}: {}", userId, e.getMessage());
                        responses.clear();
                        responses.add(ResponseBody.builder()
                                .userid(userId)
                                .response("❌ Error al generar el archivo de configuración. Por favor, contacta con el soporte.")
                                .removable(true)
                                .build());
                        // FIXME: aquí hay que notificar a los administradores del problema.
                        return responses;
                    }
                } else {
                    responses.clear();
                    responses.add(ResponseBody.builder()
                            .userid(userId)
                            .response("❌ No se pudo actualizar tu información. Por favor, intenta más tarde.")
                            .removable(true)
                            .build());
                    return responses;
                }
            } else {
                /*
                 * Si el usuario ya utilizó su plan Free entonces tiene que pagar.
                 * Se le envia un mensaje diciendole que para activar el servicio
                 * necesita realizar el pago de la suscripción.
                 */
                responses.add(ResponseBody.builder()
                        .userid(userId)
                        .response("💳 Tu plan gratuito ha expirado. Para continuar usando el servicio, necesitas renovar tu suscripción.")
                        .removable(true)
                        .build());
                return responses;
            }
        } else {
            peerDto.setActive(false);
            responses.add(ResponseBody.builder()
                    .userid(userId)
                    .response("💳 Ya tienes una configuración activa. Para crear una nueva, necesitas renovar tu suscripción.")
                    .removable(true)
                    .build());
            return responses;
        }
    }

    /**
     * Verifica que exista una instancia de Agent.
     * 
     * @return <code>true</code> en caso de que exista una.
     */
    private boolean isActiveAgent() {
        final String agentServiceName = "agent";
        if (discoveryClient != null && !discoveryClient.getInstances(agentServiceName).isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    private File buildConfig(WireguardServerProperties properties, UserDto user, PeerDto peer) throws IOException {
        StringBuilder config = new StringBuilder();
        config.append("[Interface]").append("\n");
        config.append("PrivateKey = ").append(peer.getPrivateKey()).append("\n");
        config.append("Address = ").append(peer.getIp().getIpString()).append("\n");
        config.append("DNS = 8.8.8.8, 1.1.1.1").append("\n").append("\n");

        config.append("[Peer]").append("\n");
        config.append("PublicKey = ").append(properties.getPublicKey()).append("\n");
        config.append("Endpoint = ").append(properties.getAddress()).append(":").append(properties.getPort())
                .append("\n");
        config.append("AllowedIPs = 0.0.0.0/0, ::/0").append("\n");
        config.append("PersistentKeepalive = 25").append("\n");

        Integer[] ip = peer.getIp().getIp();
        int octeto = ip[3];
        File configFile = File.createTempFile(user.getUserName()
                + octeto + properties.getName(), "conf");

        try (FileWriter writer = new FileWriter(configFile);) {
            writer.write(config.toString());
        }

        return configFile;
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
