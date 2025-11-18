package dev.yonel.wireguardbot.bot.components.custom;

import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

import lombok.Getter;
import lombok.Setter;

public class CustomEditMessageText extends EditMessageText{

    @Getter
    @Setter
    private boolean removable = false;
}
