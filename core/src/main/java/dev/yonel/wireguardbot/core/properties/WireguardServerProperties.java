package dev.yonel.wireguardbot.core.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Configuration
@ConfigurationProperties("wireguard.server")
public class WireguardServerProperties {
    private String name;
    private String address;
    private Integer port;
    private String publicKey;
}
