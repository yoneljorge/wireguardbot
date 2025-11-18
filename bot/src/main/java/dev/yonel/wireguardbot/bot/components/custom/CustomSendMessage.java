package dev.yonel.wireguardbot.bot.components.custom;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import lombok.Getter;
import lombok.Setter;

public class CustomSendMessage extends SendMessage {
    @Getter
    @Setter
    private boolean removable = false;
}
