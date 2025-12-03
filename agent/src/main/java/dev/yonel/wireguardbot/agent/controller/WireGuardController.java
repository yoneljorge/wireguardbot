package dev.yonel.wireguardbot.agent.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.yonel.wireguardbot.agent.service.WireGuardKeyService;
import dev.yonel.wireguardbot.agent.service.WireGuardPeerService;
import dev.yonel.wireguardbot.common.dtos.wireguard.WireGuardKeyPair;
import dev.yonel.wireguardbot.common.dtos.wireguard.WireGuardPeer;
import dev.yonel.wireguardbot.common.dtos.wireguard.WireGuardPeerResponse;

@RestController
@RequestMapping("/wireguard")
public class WireGuardController {

    private final WireGuardKeyService wireGuardKeyService;
    private final WireGuardPeerService wireGuardPeerService;

    public WireGuardController(WireGuardKeyService wireGuardKeyService, WireGuardPeerService wireGuardPeerService) {
        this.wireGuardKeyService = wireGuardKeyService;
        this.wireGuardPeerService = wireGuardPeerService;
    }

    @GetMapping("/generate-keys")
    public ResponseEntity<WireGuardKeyPair> generateKeyPair() {
        WireGuardKeyPair keyPair = wireGuardKeyService.generateKeyPair();
        return ResponseEntity.ok(keyPair);
    }

    @PostMapping("/{wg}/peer/add")
    public ResponseEntity<WireGuardPeerResponse> addPeer(@PathVariable String wg, @RequestBody WireGuardPeer peer) {
        try {
            wireGuardPeerService.addPeer(wg, peer);
            return ResponseEntity.ok(WireGuardPeerResponse.builder()
                    .success(true)
                    .message("Peer agregado exitosamente")
                    .data(null) // No data to return for add operation
                    .build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(WireGuardPeerResponse.builder()
                    .success(false)
                    .message(e.getMessage())
                    .data(null)
                    .build());
        } catch (RuntimeException e) {
            return ResponseEntity.internalServerError().body(WireGuardPeerResponse.builder()
                    .success(false)
                    .message("Error al agregar peer: " + e.getMessage())
                    .data(null)
                    .build());
        }
    }

    @DeleteMapping("/{wg}/peer/remove/{publicKey}")
    public ResponseEntity<WireGuardPeerResponse> removePeer(@PathVariable String wg, @PathVariable String publicKey) {
        try {
            wireGuardPeerService.removePeer(wg, publicKey);
            return ResponseEntity.ok(WireGuardPeerResponse.builder()
                    .success(true)
                    .message("Peer eliminado exitosamente")
                    .data(null)
                    .build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(WireGuardPeerResponse.builder()
                    .success(false)
                    .message(e.getMessage())
                    .data(null)
                    .build());
        } catch (RuntimeException e) {
            return ResponseEntity.internalServerError().body(WireGuardPeerResponse.builder()
                    .success(false)
                    .message("Error al eliminar peer: " + e.getMessage())
                    .data(null)
                    .build());
        }
    }

    @GetMapping("/{wg}/peer/exists/{publicKey}")
    public ResponseEntity<WireGuardPeerResponse> peerExists(@PathVariable String wg, @PathVariable String publicKey) {
        try {
            boolean exists = wireGuardPeerService.peerExists(wg, publicKey);
            return ResponseEntity.ok(WireGuardPeerResponse.builder()
                    .success(true)
                    .message(exists ? "El peer existe" : "El peer no existe")
                    .data(exists)
                    .build());
        } catch (RuntimeException e) {
            return ResponseEntity.internalServerError().body(WireGuardPeerResponse.builder()
                    .success(false)
                    .message("Error al verificar peer: " + e.getMessage())
                    .data(null)
                    .build());
        }
    }
}
