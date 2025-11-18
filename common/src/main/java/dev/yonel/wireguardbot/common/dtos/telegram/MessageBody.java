package dev.yonel.wireguardbot.common.dtos.telegram;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import dev.yonel.wireguardbot.common.enums.TypeWebhookTelegramBot;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageBody {
    private Long chatid;
    private Integer messageId;
    private Long userid;
    private Integer updateid;
    private String firstName;
    private String lastName;
    private String userName;
    private String telefono;
    private String message;
    private String fileUrl;
    @Builder.Default
    private boolean isGroup = false;
    private String groupTitle;
    @Builder.Default
    private boolean isNewUserInGroup = false;
    @Builder.Default
    private boolean isBot = false;
    private TypeWebhookTelegramBot typeBot;
}
