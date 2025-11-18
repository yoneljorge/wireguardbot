package dev.yonel.wireguardbot.bot.components.custom;

import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;

import lombok.Getter;
import lombok.Setter;

public class CustomEditMessageReplyMarkup extends EditMessageReplyMarkup {
    @Getter
    @Setter
    private boolean removable = false;
}
