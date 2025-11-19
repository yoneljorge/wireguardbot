package dev.yonel.wireguardbot.message_manager.command;

import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import lombok.Getter;

@Component
@Lazy
@Getter
public class CommandRegistryComercial {

    private final List<Command> commands;

    public CommandRegistryComercial() {
        commands = List.of();
    }
}
