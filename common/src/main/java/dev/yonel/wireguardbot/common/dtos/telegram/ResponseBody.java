package dev.yonel.wireguardbot.common.dtos.telegram;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import dev.yonel.wireguardbot.common.enums.TypeCustomKeyboardMarkup;
import dev.yonel.wireguardbot.common.enums.ParseMode;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseBody {
    private String response;
    private String chatid;
    private List<Button> buttons;
    private TypeCustomKeyboardMarkup typeKeyboard;
    @Builder.Default
    private boolean removable = false;
    private ParseMode parseMode;

    public ResponseBody(String response, String chatid) {
        this.response = response;
        this.chatid = chatid;
    }
}
