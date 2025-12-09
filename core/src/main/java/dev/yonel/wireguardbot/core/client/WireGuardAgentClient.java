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

@FeignClient(name = "wireguardbot-agent", path = "/wireguard")
public interface WireGuardAgentClient {

    @GetMapping("/generate-keys")
    ResponseEntity<WireGuardKeyPair> generateKeyPair();

    @PostMapping("/{wg}/peer/add")
    ResponseEntity<WireGuardPeerResponse> addPeer(@PathVariable String wg, @RequestBody WireGuardPeer peer);

    @DeleteMapping("/{wg}/peer/remove")
    ResponseEntity<WireGuardPeerResponse> removePeer(@PathVariable String wg, @RequestBody WireGuardPeer peer);

    @GetMapping("/{wg}/peer/exists")
    ResponseEntity<WireGuardPeerResponse> peerExists(@PathVariable String wg, @RequestBody WireGuardPeer peer);
}
