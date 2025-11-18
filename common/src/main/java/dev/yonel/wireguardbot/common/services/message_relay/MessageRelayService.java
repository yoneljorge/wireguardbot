package dev.yonel.wireguardbot.common.services.message_relay;

import java.util.List;

import org.springframework.stereotype.Service;

import dev.yonel.wireguardbot.common.dtos.telegram.MessageBody;
import dev.yonel.wireguardbot.common.dtos.telegram.ResponseBody;

@Service
public class MessageRelayService {

    public List<ResponseBody> handleMessage(MessageBody messageBody) {
        // TODO: Implementar la lógica real para manejar mensajes y generar respuestas
        // Por ahora, devolvemos una respuesta de ejemplo
        return List.of(new ResponseBody("Mensaje recibido: " + messageBody.getMessage(), String.valueOf(messageBody.getChatid())));
    }
}
