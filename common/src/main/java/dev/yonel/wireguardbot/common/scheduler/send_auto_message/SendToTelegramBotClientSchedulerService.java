
package dev.yonel.wireguardbot.common.scheduler.send_auto_message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import dev.yonel.wireguardbot.common.properties.SchedulerAutoSendMessageProperties;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SendToTelegramBotClientSchedulerService {

    /**
     * Se inyecta el bean RestTemplate
     */

    @Autowired
    private RestTemplate restTemplate;

    /**
     * URL del webhook
     */
    private final String URL;

    /**
     * Mensaje que se va a enviar en automático por el scheduler.
     */
    private final String MESSAGE;

    public SendToTelegramBotClientSchedulerService(SchedulerAutoSendMessageProperties properties) {
        this.URL = properties.getAppUrl();
        this.MESSAGE = properties.getMessage();
    }

    private ResponseEntity<String> sendWebhookUpdate(UpdateRequest updateRequest) {

        try {
            HttpEntity<UpdateRequest> requestEntity = new HttpEntity<>(updateRequest);
            return restTemplate.exchange(URL, HttpMethod.POST, requestEntity, String.class);
        } catch (ResourceAccessException e) {
            log.error("Error de conexión.");
            log.error(e.getMessage());
            // Retorna una respuesta predeterminada o vacía si prefieres evitar que el
            // programa falle
            return ResponseEntity.status(500).body("Error de conexión al webhook.");
        } catch (Exception e) {
            log.error("Error inesperado.");
            log.error(e.getMessage());
            return ResponseEntity.status(500).body("Error inesperado");
        }
    }

    public void triggerWebhook() {
        UpdateRequest updateRequest = new UpdateRequest();
        updateRequest.setUpdateId(123456789);

        UpdateRequest.Message message = new UpdateRequest.Message();
        message.setMessageId(1);

        UpdateRequest.Message.From from = new UpdateRequest.Message.From();
        from.setId(987654321);
        from.setBot(false);
        from.setFirstName("Test");
        from.setUsername("TestUser");
        message.setFrom(from);

        UpdateRequest.Message.Chat chat = new UpdateRequest.Message.Chat();
        chat.setId(987654321);
        chat.setFirstName("Test");
        chat.setUsername("TestUser");
        chat.setType("private");
        message.setChat(chat);

        message.setDate(1631579081);
        /**
         * Se establece el mensaje que se le va a enviar al bot en automático
         */
        message.setText(MESSAGE);

        updateRequest.setMessage(message);

        // Enviando el mensaje automatizado
        sendWebhookUpdate(updateRequest);
    }
}
