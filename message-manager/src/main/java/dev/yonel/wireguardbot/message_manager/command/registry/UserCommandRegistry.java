package dev.yonel.wireguardbot.message_manager.command.registry;

import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import dev.yonel.wireguardbot.message_manager.command.interfaces.Command;
import dev.yonel.wireguardbot.message_manager.command.interfaces.UserCommandInterface;
import lombok.Getter;

@Lazy
@Component
@Getter
public class UserCommandRegistry {

    private final List<? extends Command> commands;

    public UserCommandRegistry(List<UserCommandInterface> commands) {
        this.commands = commands;
    }
}
