package dev.yonel.wireguardbot.common.dtos.telegram;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Button {
    private String text;
    private String url;
    private String callbackData;
    private dev.yonel.wireguardbot.common.enums.TypeCustomButton typeButton;
}
