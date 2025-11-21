package dev.yonel.wireguardbot.message_manager.command.registry;

import java.util.List;

import org.springframework.stereotype.Component;

import dev.yonel.wireguardbot.message_manager.command.interfaces.Command;
import dev.yonel.wireguardbot.message_manager.command.interfaces.GeneralCommandInterface;
import lombok.Getter;

@Component
@Getter
public class GeneralCommandRegistry {
    private final List<? extends Command> commands;

    public GeneralCommandRegistry(List<GeneralCommandInterface> commands) {
        this.commands = commands;
    }

}
