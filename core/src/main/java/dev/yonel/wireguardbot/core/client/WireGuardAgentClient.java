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

    @PostMapping("/peer")
    ResponseEntity<WireGuardPeerResponse> addPeer(@RequestBody WireGuardPeer peer);

    @DeleteMapping("/peer/{publicKey}")
    ResponseEntity<WireGuardPeerResponse> removePeer(@PathVariable String publicKey);

    @GetMapping("/peer/{publicKey}/exists")
    ResponseEntity<WireGuardPeerResponse> peerExists(@PathVariable String publicKey);
}
