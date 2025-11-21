package dev.yonel.wireguardbot.message_manager.command.registry;

import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import dev.yonel.wireguardbot.message_manager.command.interfaces.AdminCommandInterface;
import dev.yonel.wireguardbot.message_manager.command.interfaces.Command;
import lombok.Getter;

@Lazy
@Getter
@Component
public class AdminCommandRegistry {

    private final List<? extends Command> commands;

    public AdminCommandRegistry(List<AdminCommandInterface> commands) {
        this.commands = commands;
    }
}
