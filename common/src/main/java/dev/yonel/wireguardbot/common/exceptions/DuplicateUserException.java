package dev.yonel.wireguardbot.common.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseBody
@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateUserException extends RuntimeException{

    public DuplicateUserException(String message){
        super(message);
    }

    public DuplicateUserException(String message, Throwable cause){
        super(message, cause);
    }

    public DuplicateUserException(){
        super("El usuario o recurso ya existe.");
    }

    @Override
    public String getMessage(){
        return super.getMessage();
    }
}
