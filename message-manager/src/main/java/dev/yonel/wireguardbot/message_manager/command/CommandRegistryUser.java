package dev.yonel.wireguardbot.message_manager.command;

import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import dev.yonel.wireguardbot.message_manager.command.commands.MenuCommand;
import dev.yonel.wireguardbot.message_manager.command.commands.general.StartCommand;
import dev.yonel.wireguardbot.message_manager.command.commands.user.AyudaCommand;
import lombok.Getter;

@Lazy
@Component
@Getter
public class CommandRegistryUser {

    private final List<Command> commands;

    public CommandRegistryUser(
            AyudaCommand ayudaCommand,
            StartCommand startCommand,
            MenuCommand menuCommand) {
        commands = List.of(
                startCommand,
                ayudaCommand,
                menuCommand);
    }

}
