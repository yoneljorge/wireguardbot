package dev.yonel.wireguardbot.core.commands.user.configuracion.utils;

import dev.yonel.wireguardbot.common.dtos.PeerDto;
import dev.yonel.wireguardbot.common.dtos.UserDto;
import dev.yonel.wireguardbot.core.properties.WireguardServerProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


@Component
public class ConfiguracionCommandUtils {

    @Autowired
    private DiscoveryClient discoveryClient;

    public File buildConfig(
            WireguardServerProperties properties,
            UserDto user,
            PeerDto peer) throws IOException {
        StringBuilder config = new StringBuilder();
        config.append("[Interface]").append("\n");
        config.append("PrivateKey = ").append(peer.getPrivateKey()).append("\n");
        config.append("Address = ").append(peer.getIpDto().getIpString()).append("\n");
        config.append("DNS = 8.8.8.8, 1.1.1.1").append("\n").append("\n");

        config.append("[Peer]").append("\n");
        config.append("PublicKey = ").append(properties.getPublicKey()).append("\n");
        config.append("Endpoint = ").append(properties.getAddress()).append(":").append(properties.getPort())
                .append("\n");
        config.append("AllowedIPs = 0.0.0.0/0, ::/0").append("\n");
        config.append("PersistentKeepalive = 25").append("\n");

        Integer[] ip = peer.getIpDto().getIp();
        int octeto = ip[3];
        File configFile = File.createTempFile(user.getUserName()
                + octeto + properties.getName(), "conf");

        try (FileWriter writer = new FileWriter(configFile);) {
            writer.write(config.toString());
        }

        return configFile;
    }

    /**
     * Verifica que exista una instancia de Agent.
     *
     * @return <code>true</code> en caso de que exista una.
     */
    public boolean isNotActiveAgent() {
        final String agentServiceName = "wireguardbot-agent";
        return !(discoveryClient != null && !discoveryClient.getInstances(agentServiceName).isEmpty());
    }
}
