package dev.yonel.wireguardbot.common.exceptions;

/**
 * Excepción que se lanza cuando un existe un usuario con ese identificador..
 */
public class DuplicateUserException extends WireguardBotExceptions{

    public DuplicateUserException(){
        super("El usuario o recurso ya existe.");
    }

    public DuplicateUserException(String message){
        super(message);
    }

    public DuplicateUserException(String message, Throwable cause){
        super(message, cause);
    }

    @Override
    public String getMessage(){
        return super.getMessage();
    }
}
