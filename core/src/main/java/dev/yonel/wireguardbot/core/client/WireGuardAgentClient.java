package dev.yonel.wireguardbot.core.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import dev.yonel.wireguardbot.common.dtos.wireguard.WireGuardKeyPair;
import dev.yonel.wireguardbot.common.dtos.wireguard.WireGuardPeer;
import dev.yonel.wireguardbot.common.dtos.wireguard.WireGuardPeerResponse;

@FeignClient(name = "agent", path = "/wireguard")
public interface WireGuardAgentClient {

    @GetMapping("/generate-keys")
    ResponseEntity<WireGuardKeyPair> generateKeyPair();

    @PostMapping("/{wg}/peer")
    ResponseEntity<WireGuardPeerResponse> addPeer(@PathVariable("wg") String wg, @RequestBody WireGuardPeer peer);

    @DeleteMapping("/{wg}/peer/{publicKey}")
    ResponseEntity<WireGuardPeerResponse> removePeer(@PathVariable("wg") String wg, @PathVariable("publicKey") String publicKey);

    @GetMapping("/{wg}/peer/{publicKey}/exists")
    ResponseEntity<WireGuardPeerResponse> peerExists(@PathVariable("wg") String wg, @PathVariable("publicKey") String publicKey);
}
