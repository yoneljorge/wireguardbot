package dev.yonel.wireguardbot.bot.components.custom;

import org.telegram.telegrambots.meta.api.methods.send.SendDocument;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomSendDocument extends SendDocument{
    @Getter
    @Setter
    private boolean removable = false;
}
