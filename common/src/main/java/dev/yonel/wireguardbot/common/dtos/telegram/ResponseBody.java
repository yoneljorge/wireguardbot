package dev.yonel.wireguardbot.common.dtos.telegram;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import dev.yonel.wireguardbot.common.enums.TypeCustomKeyboardMarkup;
import dev.yonel.wireguardbot.common.enums.TypeParseMode;
import dev.yonel.wireguardbot.common.enums.TypeSendExecution;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ResponseBody {

    private Integer messageId;
    private Long chatid;
    private Long userid;
    private String response;
    private int updateid;
    
    private TypeParseMode parseMode;
    private TypeSendExecution typeSendExecution;

    private List<Button> buttons;
    private TypeCustomKeyboardMarkup typeKeyboard;

    private String fileDocument;
    @Builder.Default
    private boolean removable = false;
}