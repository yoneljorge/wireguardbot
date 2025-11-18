package dev.yonel.wireguardbot.common.dtos.wireguard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO de respuesta para operaciones de peers de WireGuard
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WireGuardPeerResponse {
    private boolean success;
    private String message;
    private Object data;
}
