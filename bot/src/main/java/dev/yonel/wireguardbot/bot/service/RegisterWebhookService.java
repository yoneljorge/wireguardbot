
package dev.yonel.wireguardbot.bot.service;

import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RegisterWebhookService {

    static final String API_URL = "https://api.telegram.org/bot";

    @Value("${app.url}")
    private String appUrl;

    public void registerWebhook(String token, String path){
        final OkHttpClient okHttpClient = new OkHttpClient();

        String apiUrl = API_URL + token + "/setWebhook";
        String webhookUrl = appUrl + path;

        String jsonBody = "{ \"url\": \"" + webhookUrl + "\" }";

        RequestBody body = RequestBody.create(
                jsonBody,
                MediaType.parse("application/json")
        );

        Request request = new Request.Builder()
                .url(apiUrl)
                .post(body)
                .build();

        try(Response response = okHttpClient.newCall(request).execute()){
            if(!response.isSuccessful()){
                throw new IllegalStateException("Error al registrar webhook: HTTP " + response.code());
            }

            String responseBody = response.body() != null ? response.body().string() : "";

            log.info("Webhook '{}' has been enabled -> {}", webhookUrl, responseBody);
        }catch (Exception e){
            log.error("Webhook '{}' couldn't be enabled", webhookUrl);
            e.printStackTrace();
        }
    }
}
