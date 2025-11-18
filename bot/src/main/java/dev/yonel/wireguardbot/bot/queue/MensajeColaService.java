
package dev.yonel.wireguardbot.bot.queue;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import dev.yonel.wireguardbot.bot.service.WebhookBotAdmin;
import dev.yonel.wireguardbot.bot.service.WebhookBotClient;
import dev.yonel.wireguardbot.common.enums.TypeWebhookTelegramBot;
import dev.yonel.wireguardbot.common.properties.SchedulerAutoSendMessageProperties;
import lombok.extern.slf4j.Slf4j;

@Lazy
@Slf4j
@Service
public class MensajeColaService {

    @Autowired
    private WebhookBotClient webhookBotClient;

    @Autowired
    private WebhookBotAdmin webhookBotAdmin;

    @Autowired
    private SchedulerAutoSendMessageProperties schedulerProperties;

    private static final ConcurrentLinkedQueue<IncomingMessageTelegram> colaDeMensajes = new ConcurrentLinkedQueue<>();
    private static final ConcurrentHashMap<String, Boolean> mensajesProcesados = new ConcurrentHashMap<>();

    // FIXME: Agregar funcionalidad para procesar stickers
    public void encolarMensaje(IncomingMessageTelegram mensaje) {
        String text;
        if (mensaje.getUpdateMessage().getMessage() != null) {
            text = mensaje.getUpdateMessage().getMessage().getText();
        } else {
            text = mensaje.getUpdateMessage().getCallbackQuery().getData();
        }

        if (text != null) {
            if (!text.equals(schedulerProperties.getMessage())) {
                //FIXME quitar esto en produccion
                log.info("Nuevo mensaje: {}", text);
                String mensajeIdUnico = generarIdUnico(mensaje);
                if (!mensajesProcesados.containsKey(mensajeIdUnico)) {

                    //FIXME quitar esto en produccion
                    log.info("Mensaje encolado: {}", text);
                    colaDeMensajes.offer(mensaje);
                    mensajesProcesados.put(mensajeIdUnico, true);
                    procesarMensajesEnCola(); // Iniciar el procesamiento si es necesario
                } else {
                    log.info("Mensaje no encolado: {}", text);
                }
            }
        }
    }

    @Async
    public void procesarMensajesEnCola() {
        IncomingMessageTelegram mensaje;
        while ((mensaje = colaDeMensajes.poll()) != null) {
            try {
                // Lógica de procesamiento del mensaje y respuesta al bot de Telegram
                log.info("Procesando mensaje");

                if (mensaje.getBot() == TypeWebhookTelegramBot.ADMIN) {
                    webhookBotAdmin.onWebhookUpdateReceived(mensaje.getUpdateMessage());
                } else if (mensaje.getBot() == TypeWebhookTelegramBot.CLIENT) {
                    webhookBotClient.onWebhookUpdateReceived(mensaje.getUpdateMessage());
                }

                log.info("Mensaje procesado: " + generarIdUnico(mensaje));
            } catch (Exception e) {
                Thread.currentThread().interrupt();
                // Considerar reenviar a una cola de errores local si es necesario
                log.error("Error al procesar el mensaje: {} - {}", generarIdUnico(mensaje), e.getMessage());
            }
        }
    }

    private static String generarIdUnico(IncomingMessageTelegram mensaje) {
        // FIXME: sout de prueba eliminar
        String id = "bot:" + mensaje.getBot() + ":" + mensaje.getUserid() + ":" + mensaje.getUpdateid();
        System.out.println(">>>>>>>>>IdIncomingMessage: " + id);
        return id;
    }

    private static String generarIdUnico(TypeWebhookTelegramBot bot, String userid, int updateid) {
        String id = "bot:" + bot + ":" + userid + ":" + updateid;
        System.out.println(">>>>>>>>>IdRemovingMessage: " + id);
        return id;
    }

    public static void removeMensajesProcesados(TypeWebhookTelegramBot bot, String userid, int updateid) {
        try {
            mensajesProcesados.remove(generarIdUnico(bot, userid, updateid));
            log.info("Removiendo mensaje procesado");
        } catch (Exception e) {
            log.error("Error eliminando mensaje procesado.");
        }
    }
}
