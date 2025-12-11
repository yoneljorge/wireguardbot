package dev.yonel.wireguardbot.bot.components;

import dev.yonel.wireguardbot.common.dtos.telegram.Button;
import dev.yonel.wireguardbot.common.dtos.telegram.ResponseBody;
import dev.yonel.wireguardbot.common.enums.TypeCustomButton;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ForceReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Lazy
@Component
public class TelegramButtons {

    /**
     * Método con el cual le agregamos los botones directamente a los mensajes y
     * pueden ejecutar acciones específicas o abrir enlaces. Se debe utilizar para
     * opciones rápidas e interactivas.
     */
    public static InlineKeyboardMarkup getInlineKeyboard(ResponseBody responseBody) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();
        int contador = 0;
        int rows;
        if(responseBody.getRows() != 0){
            rows = responseBody.getRows();
        }else {
            rows = 3;
        }


        for (Button button : responseBody.getButtons()) {

            InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();

            inlineKeyboardButton.setText(button.getText());
            if (button.getTypeButton() == TypeCustomButton.URL) {
                inlineKeyboardButton.setUrl(button.getUrl());
            } else if (button.getTypeButton() == TypeCustomButton.CALLBACKDATA) {
                inlineKeyboardButton.setCallbackData(button.getCallbackData());
            }
            row.add(inlineKeyboardButton);
            contador++;
            if (contador == rows) {
                keyboard.add(row);
                row = new ArrayList<>();
                contador = 0;
            }
        }
        if (contador != 0 && contador < rows) {
            keyboard.add(row);
        }
        markup.setKeyboard(keyboard);
        return markup;
    }

    /**
 * Método que crea botones que aparecen como un teclado virtual en el chat y
 * permiten al usuario seleccionar respuestas predefinidas.
 * El usuario enviará el texto del botón como mensaje normal.
 */
    public static ReplyKeyboardMarkup getReplyKeyboard(ResponseBody responseBody) {

        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow currentRow = new KeyboardRow();
        int contador = 0;
        int rows;
        if(responseBody.getRows() != 0){
            rows = responseBody.getRows();
        }else {
            rows = 3;
        }

        for(Button button : responseBody.getButtons()){
            KeyboardButton keyboardButton = new KeyboardButton(button.getText());
            
            currentRow.add(keyboardButton);
            contador ++;

            // Organizar en filas en dependencia de la cantidad de rows
            if(contador == rows){
                keyboard.add(currentRow);
                currentRow = new KeyboardRow();
                contador = 0;
            }
        }

        // Agregar la última fila si tiene botones
        if(contador > 0){
            keyboard.add(currentRow);
        }

        markup.setKeyboard(keyboard);
        markup.setResizeKeyboard(true); // Ajustar tamaño al contenido
        markup.setOneTimeKeyboard(true); // Ocultar el teclado después de seleccionar una opción

        return markup;
    }

    /**
     * Método con el cual se oculta el teclado del usuario después de que seleccione
     * una opción.
     */
    public static ReplyKeyboardRemove removeReplyKeyboard() {
        ReplyKeyboardRemove removeKeyboard = new ReplyKeyboardRemove();
        removeKeyboard.setRemoveKeyboard(true);
        return removeKeyboard;
    }

    public void forceReply() {
        ForceReplyKeyboard forceReplyKeyboard = new ForceReplyKeyboard();
        forceReplyKeyboard.setSelective(true);
    }
}
