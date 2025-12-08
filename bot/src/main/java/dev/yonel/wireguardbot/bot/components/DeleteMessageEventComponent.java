package dev.yonel.wireguardbot.bot.components;

import dev.yonel.wireguardbot.common.context.UserSessionContext;
import dev.yonel.wireguardbot.common.enums.TypeWebhookTelegramBot;
import dev.yonel.wireguardbot.common.events.DeleteMessageEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class DeleteMessageEventComponent {

    @Autowired
    private ApplicationEventPublisher publisher;

    public void deleteMessages(String chatId, UserSessionContext context, TypeWebhookTelegramBot bot){
        if(context.getBotSession(bot).isPendingMessageToDelete()){
            for(int messageId : context.getBotSession(bot).getMessagesIdToDelete()){
              publisher.publishEvent(new DeleteMessageEvent(this,chatId, messageId));
            }
        }
    }
}
