package dev.yonel.wireguardbot.bot.service;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import dev.yonel.wireguardbot.common.events.MessageRelayToTelegramBotAdminEvent;

public interface WebhookBotAdmin {
    BotApiMethod<?> onWebhookUpdateReceived(Update update);

    void handleMessageRelayToTelegramBotAdminEvent(MessageRelayToTelegramBotAdminEvent event);
}
