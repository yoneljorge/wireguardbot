/**
 * Servicio encargado de registrar el webhook de un bot de Telegram de forma programática.
 *
 * <p>Esta clase recibe una instancia de {@link TelegramWebhookBot} y utiliza su token
 * y su ruta interna (obtenida desde {@code getBotPath()}) para construir la URL pública
 * completa a la que Telegram enviará las actualizaciones (updates).
 *
 * <p>El procedimiento es:
 * <ol>
 *     <li>Construir la URL final del webhook usando {@code app.url + getBotPath()}.</li>
 *     <li>Crear una instancia de {@link com.pengrad.telegrambot.TelegramBot} usando el token
 *         del bot recibido.</li>
 *     <li>Enviar la petición {@link SetWebhook} a la API de Telegram utilizando la librería
 *         de Pengrad (más ligera y adecuada para llamadas HTTP directas).</li>
 *     <li>Validar que la respuesta sea exitosa; si no lo es, se arroja una excepción.</li>
 * </ol>
 *
 * <p>Este servicio permite registrar webhooks para múltiples bots dentro de la misma
 * aplicación, sin acoplar la lógica al ciclo de vida de Spring. El método
 * {@code registerWebhook()} puede llamarse durante el arranque de la aplicación o
 * en cualquier momento según la necesidad.
 *
 * <p>Dependencias clave:
 * <ul>
 *     <li>{@code app.url} — URL pública base donde está expuesta la aplicación.</li>
 *     <li>{@link TelegramWebhookBot#getBotPath()} — path interno del endpoint asociado al bot.</li>
 *     <li>{@link TelegramWebhookBot#getBotToken()} — token necesario para autenticar la llamada a Telegram.</li>
 * </ul>
 *
 * <p>En caso de éxito, se registra el webhook y se genera un log informativo.
 * En caso de fallo, se genera un log de error y la excepción es propagada.
 */

package dev.yonel.wireguardbot.bot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SetWebhook;
import com.pengrad.telegrambot.response.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramWebhookBot;

@Slf4j
@Component
public class RegisterWebhookService {

    @Value("${app.url}")
    private String appUrl;

    /**
     * Registra el webhook de un bot de Telegram utilizando la API HTTP de Telegram
     * mediante la librería Pengrad.
     *
     * <p>Este método construye dinámicamente la URL final del webhook combinando
     * la URL pública configurada en {@code app.url} y el path interno obtenido desde
     * {@link TelegramWebhookBot#getBotPath()}. Posteriormente, envía una petición
     * {@link SetWebhook} a la API de Telegram utilizando el token del bot recibido.
     *
     * <p>El registro del webhook es idempotente: si el webhook ya está configurado
     * con la misma URL, Telegram simplemente devolverá una respuesta exitosa.
     *
     * <p>Flujo del método:
     * <ol>
     *     <li>Construcción de la URL completa del webhook.</li>
     *     <li>Creación de un cliente {@link com.pengrad.telegrambot.TelegramBot} usando el token del bot.</li>
     *     <li>Ejecución de la operación {@code setWebhook} contra los servidores de Telegram.</li>
     *     <li>Validación de que la respuesta fue exitosa.</li>
     * </ol>
     *
     * @param webhookBot instancia del bot que extiende {@link TelegramWebhookBot},
     *                   desde donde se obtiene el token y el path del webhook.
     *
     * @throws IllegalStateException si la API de Telegram devuelve una respuesta no exitosa.
     * @throws Exception si ocurre un error inesperado al ejecutar la solicitud HTTP.
     */
    public void registerWebhook(TelegramWebhookBot webhookBot){
        String url = appUrl + webhookBot.getBotPath();
        TelegramBot bot = new TelegramBot(webhookBot.getBotToken());

        SetWebhook request = new SetWebhook();
        request.url(url);

        try {
            BaseResponse response = bot.execute(request);
            if(!response.isOk()) throw new IllegalStateException();
            log.info("Webhook '{}' has been enabled", url);
        } catch (Exception e) {
            log.error("Webhook '{}' couldn't be enabled", url);
            throw e;
        }
    }
}
