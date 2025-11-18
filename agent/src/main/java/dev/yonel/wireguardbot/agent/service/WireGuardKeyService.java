package dev.yonel.wireguardbot.agent.service;

import java.security.SecureRandom;
import java.util.Base64;

import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.generators.X25519KeyPairGenerator;
import org.bouncycastle.crypto.params.X25519KeyGenerationParameters;
import org.bouncycastle.crypto.params.X25519PrivateKeyParameters;
import org.bouncycastle.crypto.params.X25519PublicKeyParameters;
import org.springframework.stereotype.Service;

import dev.yonel.wireguardbot.common.dtos.wireguard.WireGuardKeyPair;

/**
 * Servicio para generar pares de claves WireGuard (pública y privada)
 * usando el algoritmo Curve25519 (X25519)
 */
@Service
public class WireGuardKeyService {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    /**
     * Genera un nuevo par de claves WireGuard (pública y privada)
     * 
     * @return WireGuardKeyPair con la clave pública y privada en formato Base64
     */
    public WireGuardKeyPair generateKeyPair() {
        // Generar par de claves usando X25519 (Curve25519)
        X25519KeyPairGenerator keyPairGenerator = new X25519KeyPairGenerator();
        keyPairGenerator.init(new X25519KeyGenerationParameters(SECURE_RANDOM));
        AsymmetricCipherKeyPair keyPair = keyPairGenerator.generateKeyPair();

        // Extraer la clave privada
        X25519PrivateKeyParameters privateKeyParams = (X25519PrivateKeyParameters) keyPair.getPrivate();
        byte[] privateKeyBytes = privateKeyParams.getEncoded();

        // Extraer la clave pública
        X25519PublicKeyParameters publicKeyParams = (X25519PublicKeyParameters) keyPair.getPublic();
        byte[] publicKeyBytes = publicKeyParams.getEncoded();

        // Codificar en Base64 (formato estándar de WireGuard)
        String privateKeyBase64 = Base64.getEncoder().encodeToString(privateKeyBytes);
        String publicKeyBase64 = Base64.getEncoder().encodeToString(publicKeyBytes);

        return WireGuardKeyPair.builder()
                .privateKey(privateKeyBase64)
                .publicKey(publicKeyBase64)
                .build();
    }
}

