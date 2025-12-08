
package dev.yonel.wireguardbot.bot.components;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import dev.yonel.wireguardbot.common.dtos.telegram.MessageBody;
import dev.yonel.wireguardbot.common.enums.TypeWebhookTelegramBot;
import dev.yonel.wireguardbot.common.events.RegisterNewUsersFromGroupEvent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class TelegramUserManager {

    @Autowired
    private ApplicationEventPublisher publisher;

    public MessageBody createMessageBody(TypeWebhookTelegramBot bot, Update update, String fileUrl) {
        MessageBody messageBody = new MessageBody();

        if (update.getMessage() != null){
            Message message = update.getMessage();
            User user = message.getFrom();
            // Validar que el usuario no sea nulo
            if (user == null) {
                log.warn("Usuario nulo en update: {}", update);
                return messageBody; // Retornamos un MessageBody vacío pero no nulo
            }
            messageBody.setChatid(message.getChatId());
            messageBody.setMessageId(message.getMessageId());
            messageBody.setUserid(user.getId());
            // Usar valores por defecto si alguno es nulo
            messageBody.setFirstName(user.getFirstName() != null ? user.getFirstName() : "");
            messageBody.setLastName(user.getLastName() != null ? user.getLastName() : "");
            messageBody.setUserName(user.getUserName() != null ? user.getUserName() : "");
            messageBody.setMessage(message.getText() != null ? message.getText() : "");
        }


        if (update.getCallbackQuery() != null) {
            messageBody.setChatid(update.getCallbackQuery().getMessage().getChatId());
            messageBody.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
            messageBody.setUserid(update.getCallbackQuery().getFrom().getId());

            messageBody.setFirstName(update.getCallbackQuery().getFrom().getFirstName());
            messageBody.setLastName(update.getCallbackQuery().getFrom().getLastName());
            messageBody.setUserName(update.getCallbackQuery().getFrom().getUserName());
            messageBody.setMessage(update.getCallbackQuery().getData());
        }

        messageBody.setUpdateid(update.getUpdateId());
        messageBody.setTelefono(extractPhoneNumber(update));
        messageBody.setFileUrl(fileUrl);
        messageBody.setTypeBot(bot);

        return messageBody;
    }

    public void handleNewGroupUsers(List<User> newUsers, TypeWebhookTelegramBot bot) {
        List<MessageBody> listMessageBody = new ArrayList<>();

        for (User user : newUsers) {
            MessageBody messageBody = new MessageBody();
            messageBody.setUserid(user.getId());
            messageBody.setChatid(user.getId());
            messageBody.setFirstName(user.getFirstName());
            messageBody.setLastName(user.getLastName());
            messageBody.setUserName(user.getUserName());
            messageBody.setGroup(true);
            messageBody.setNewUserInGroup(true);
            messageBody.setBot(user.getIsBot());
            messageBody.setTypeBot(bot);
            listMessageBody.add(messageBody);
        }

        publisher.publishEvent(new RegisterNewUsersFromGroupEvent(this, listMessageBody));
    }

    private String extractPhoneNumber(Update update) {
        try {
            Contact contact = update.getMessage().getContact();
            return contact.getPhoneNumber();
        } catch (Exception e) {
            return "";
        }
    }
}
