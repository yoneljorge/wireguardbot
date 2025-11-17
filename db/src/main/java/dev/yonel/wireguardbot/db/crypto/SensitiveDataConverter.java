package dev.yonel.wireguardbot.db.crypto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dev.yonel.wireguardbot.common.crypto.EncryptionService;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Component
@Converter(autoApply = false) // significa que debemos de aplicarlo con @Converter en cada campo
public class SensitiveDataConverter implements AttributeConverter<String, String>{

    @Autowired
    private EncryptionService encryptionService;

    /**
     * Se ejecuta antes de que el dato sea guardado en la Base de Datos.
     */
    @Override
    public String convertToDatabaseColumn(String attribute) {
        if(attribute == null){
            return null;
        }
        return encryptionService.encrypt(attribute);
    }

    /**
     * Se ejecuta después de que el dato el leído de la Base de Datos.
     */
    @Override
    public String convertToEntityAttribute(String dbData) {
        if(dbData == null){
            return null;
        }
        return encryptionService.decrypt(dbData);
    }

}
