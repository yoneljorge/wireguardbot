package dev.yonel.wireguardbot.core.commands.user.configuracion;

import dev.yonel.wireguardbot.message_manager.command.interfaces.Command;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ConfiguracionCommandFactory {

    @Autowired
    private CrearConfiguracionCommand crearConfiguracionCommand;
    @Autowired
    private EliminarConfiguracionCommand eliminarConfiguracionCommand;
    @Autowired
    private ObtenerConfiguracionCommand obtenerConfiguracionCommand;

    public Command getCommand(String commandName) {
        if (commandName == null) {
            return null;
        }
        if (commandName.equalsIgnoreCase(CrearConfiguracionCommand.NAME)) {
            return this.crearConfiguracionCommand;
        }
        
        if(commandName.equalsIgnoreCase(EliminarConfiguracionCommand.NAME)){
            return this.eliminarConfiguracionCommand;
        }

        if(commandName.equalsIgnoreCase(ObtenerConfiguracionCommand.NAME)){
            return this.eliminarConfiguracionCommand;
        }

        return null;
    }
}
