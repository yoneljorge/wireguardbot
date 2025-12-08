package dev.yonel.wireguardbot.bot.service;

import dev.yonel.wireguardbot.common.events.DeleteMessageEvent;

public interface MessageDeletionHandler {

    void handleDeleteMessage(DeleteMessageEvent event);
}
