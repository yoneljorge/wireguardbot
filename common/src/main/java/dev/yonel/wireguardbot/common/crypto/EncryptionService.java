package dev.yonel.wireguardbot.common.crypto;

import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

@Service
public class EncryptionService {
    private static final String ALGORITHM = "AES";
    private SecretKeySpec keySpec;
    @Value("${encryption.service.key}")
    private String secretKey;

    @PostConstruct
    public void init(){
        if(secretKey == null || (secretKey.length() != 16 && secretKey.length() != 24 && secretKey.length() != 32)){
            throw new IllegalArgumentException("La clave de cifrado tiene que ser de 16, 24 o 32 caracteres para AES");
        }
        this.keySpec = new SecretKeySpec(secretKey.getBytes(), ALGORITHM);
    }

    public String encrypt(String value) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            byte[] encryptedValue = cipher.doFinal(value.getBytes());
            return Base64.getEncoder().encodeToString(encryptedValue);
        } catch (Exception e) {
            throw new RuntimeException("Error cifrando datos", e);
        }
    }

    public String decrypt(String encryptedValue) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            byte[] decodedValue = Base64.getDecoder().decode(encryptedValue);
            byte[] decryptedValue = cipher.doFinal(decodedValue);
            return new String(decryptedValue);
        } catch (Exception e) {
            // Manejar la excepción, por ejemplo, si el formato no es correcto.
            throw new RuntimeException("Error al descifrar datos", e);
        }
    }
}
