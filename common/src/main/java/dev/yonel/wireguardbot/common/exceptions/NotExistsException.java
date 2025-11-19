package dev.yonel.wireguardbot.common.exceptions;


public class NotExistsException extends WireguardBotExceptions{

    public NotExistsException(){
        super();
    }

    public NotExistsException(String message){
        super(message);
    }

    public NotExistsException(String message, Throwable cause){
        super(message, cause);
    }

    @Override
    public String getMessage(){
        return super.getMessage();
    }

}
