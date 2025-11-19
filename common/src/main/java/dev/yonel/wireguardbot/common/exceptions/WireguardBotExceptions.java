package dev.yonel.wireguardbot.common.exceptions;

import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepción base para las excepciones de la aplicación.
 */
@ResponseBody
@ResponseStatus
public class WireguardBotExceptions extends RuntimeException{


    public WireguardBotExceptions(){
        super("Error inesperado en la aplicación. Consulte el log para más detalles.");
    }

    public WireguardBotExceptions(Throwable cause){
        super(cause);
    }
    
    public WireguardBotExceptions(String message, Throwable cause){
        super(message, cause);
    }
    
    public WireguardBotExceptions(String message){
        super(message);
    }

    @Override
    public String getMessage(){
        return super.getMessage();
    }
}
