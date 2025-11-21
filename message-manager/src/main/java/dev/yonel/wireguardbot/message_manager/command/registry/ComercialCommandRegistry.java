package dev.yonel.wireguardbot.message_manager.command.registry;

import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import dev.yonel.wireguardbot.message_manager.command.interfaces.ComercialCommandInterface;
import dev.yonel.wireguardbot.message_manager.command.interfaces.Command;
import lombok.Getter;

@Component
@Lazy
@Getter
public class ComercialCommandRegistry {

    private final List<? extends Command>  commands;

    public ComercialCommandRegistry(List<ComercialCommandInterface> commands) {
        this.commands = commands;
    }
}
