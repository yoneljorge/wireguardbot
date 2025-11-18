package dev.yonel.wireguardbot.bot.components;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import java.io.Serializable;

public class EmptyResponse extends BotApiMethod<EmptyResponse.EmptySerializableResponse>{

    @JsonIgnore
    @Override
    public EmptySerializableResponse deserializeResponse(String answer) throws TelegramApiRequestException {
        return null;
    }

    @JsonIgnore
    @Override
    public String getMethod() {
        return ""; // No se ejecuta nada
    }

    public static class EmptySerializableResponse implements Serializable {}
}


