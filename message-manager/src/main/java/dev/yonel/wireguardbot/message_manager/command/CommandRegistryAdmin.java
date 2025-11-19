package dev.yonel.wireguardbot.message_manager.command;

import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import lombok.Getter;

@Lazy
@Getter
@Component
public class CommandRegistryAdmin {

    private final List<Command> commands;

    public CommandRegistryAdmin() {
        commands = List.of();
    }
}
