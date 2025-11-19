package dev.yonel.wireguardbot.common.events;

import java.util.EventObject;
import java.util.List;

import dev.yonel.wireguardbot.common.dtos.telegram.MessageBody;
import lombok.Getter;

@Getter
public class RegisterNewUsersFromGroupEvent extends EventObject {
    private final List<MessageBody> newUsers;

    public RegisterNewUsersFromGroupEvent(Object source, List<MessageBody> newUsers){
        super(source);
        this.newUsers = newUsers;
    }
}
