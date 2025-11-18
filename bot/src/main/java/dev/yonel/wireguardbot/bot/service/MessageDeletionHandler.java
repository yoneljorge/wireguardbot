package dev.yonel.wireguardbot.bot.service;

import dev.yonel.wireguardbot.common.events.MessageRelayToDeleteMessageEvent;

public interface MessageDeletionHandler {

    void handleMessageRelayToDeleteMessage(MessageRelayToDeleteMessageEvent event);
}
