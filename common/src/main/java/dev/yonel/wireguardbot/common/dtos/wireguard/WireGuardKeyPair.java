package dev.yonel.wireguardbot.common.dtos.wireguard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO que representa un par de claves WireGuard (pública y privada)
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WireGuardKeyPair {
    /**
     * Clave pública de WireGuard en formato Base64
     */
    private String publicKey;
    
    /**
     * Clave privada de WireGuard en formato Base64
     */
    private String privateKey;
}

