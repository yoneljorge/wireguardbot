package dev.yonel.wireguardbot.common.dtos.wireguard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO que representa un peer de WireGuard
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WireGuardPeer {
    /**
     * Clave pública del peer en formato Base64
     */
    private String publicKey;
    
    /**
     * IP permitida para el peer (ej: 10.0.0.2/32)
     */
    private String allowedIp;
    
    /**
     * Endpoint del peer (opcional, formato: host:port)
     * Solo necesario si el peer es otro servidor WireGuard
     */
    private String endpoint;
    
    /**
     * IPs permitidas adicionales (opcional)
     */
    private String[] allowedIps;
}

