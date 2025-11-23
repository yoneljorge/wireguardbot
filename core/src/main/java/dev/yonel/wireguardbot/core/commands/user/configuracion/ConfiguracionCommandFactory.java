package dev.yonel.wireguardbot.core.commands.user.configuracion;

import dev.yonel.wireguardbot.message_manager.command.interfaces.Command;

public class ConfiguracionCommandFactory {

    public static Command getCommand(String commandName) {
        if (commandName == null) {
            return null;
        }
        if (commandName.equalsIgnoreCase(CrearConfiguracionCommand.NAME)) {
            return new CrearConfiguracionCommand();
        }
        
        if(commandName.equalsIgnoreCase(EliminarConfiguracionCommand.NAME)){
            return new EliminarConfiguracionCommand();
        }

        if(commandName.equalsIgnoreCase(GestionarConfiguracionCommand.NAME)){
            return new GestionarConfiguracionCommand();
        }

        if(commandName.equalsIgnoreCase(ObtenerConfiguracionCommand.NAME)){
            return new ObtenerConfiguracionCommand();
        }

        return null;
    }
}
