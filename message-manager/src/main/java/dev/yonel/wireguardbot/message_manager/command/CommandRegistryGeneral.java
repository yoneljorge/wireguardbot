package dev.yonel.wireguardbot.message_manager.command;

import java.util.List;

import org.springframework.stereotype.Component;

import dev.yonel.wireguardbot.message_manager.command.commands.general.SalirCommand;
import lombok.Getter;

@Component
@Getter
public class CommandRegistryGeneral {
    private final List<Command> commands;

    public CommandRegistryGeneral(SalirCommand salirCommand) {
        commands = List.of(
                salirCommand);
    }

}
