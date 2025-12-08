package dev.yonel.wireguardbot.message_manager.command;

import java.util.ArrayList;
import java.util.List;

import dev.yonel.wireguardbot.common.dtos.telegram.MessageBody;
import dev.yonel.wireguardbot.common.dtos.telegram.ResponseBody;

public class CommandBase {

    protected CommandBase(){
        initialize();
    }
    
    private List<ResponseBody> responses;

    protected void initialize() {
        responses = new ArrayList<>();
    }

    protected List<ResponseBody> getResponses() {
        return responses;
    }

    protected void addResponse(ResponseBody response) {
        if (response != null) {
            responses.add(response);
        }
    }

    protected void addResponses(List<ResponseBody> newResponses) {
        for (ResponseBody responseBody : newResponses) {
            this.responses.add(responseBody);
        }
    }

    protected void createNewResponse(MessageBody messageBody) {
        ResponseBody response = new ResponseBody();
        response.setChatid(messageBody.getChatid());
        response.setUserid(messageBody.getUserid());
        responses.add(response);
    }

    protected void createNewResponse(MessageBody messageBody, String responseText) {
        ResponseBody response = new ResponseBody();
        response.setChatid(messageBody.getChatid());
        response.setUserid(messageBody.getUserid());
        response.setResponse(responseText);
        responses.add(response);
    }

    protected void createNewResponse(Long chatid, Long userid, String responseText) {
        ResponseBody response = new ResponseBody();
        response.setChatid(chatid);
        response.setUserid(userid);
        response.setResponse(responseText);
        responses.add(response);
    }

    protected ResponseBody getCurrentResponse() {
        if (responses.isEmpty()) {
            throw new IllegalStateException("No hay respuestas inicializadas");
        }
        return responses.getLast();
    }

    protected void updateCurrentResponse(ResponseBody responseBody){
        if (responseBody == null) {
            throw new IllegalArgumentException("ResponseBody no puede ser null.");
        }
        this.responses.add(this.responses.size() -1, responseBody);

    }

    protected ResponseBody getResponse(int index) {
        if (index < 0 || index >= responses.size()) {
            throw new IndexOutOfBoundsException("Índice de respuesta inválido");
        }
        return responses.get(index);
    }

    protected int getResponseCount() {
        return responses.size();
    }

    protected void removeCurrentResponse() {
        if (responses.isEmpty()) {
            throw new IllegalStateException("No hay respuestas inicializadas");
        }
        
        responses.remove(responses.size() - 1);
    }

    protected void clearResponses() {
        responses.clear();
    }
}
