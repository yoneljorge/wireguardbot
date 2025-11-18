package dev.yonel.wireguardbot.common.events;

import java.util.EventObject;
import java.util.List;

import dev.yonel.wireguardbot.common.dtos.telegram.MessageBody;

public class RegisterNewUsersFromGroupEvent extends EventObject {
    private final List<MessageBody> users;

    public RegisterNewUsersFromGroupEvent(Object source, List<MessageBody> users) {
        super(source);
        this.users = users;
    }

    public List<MessageBody> getUsers() {
        return users;
    }
}
