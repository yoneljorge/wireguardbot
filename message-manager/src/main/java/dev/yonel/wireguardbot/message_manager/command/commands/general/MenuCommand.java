
package dev.yonel.wireguardbot.message_manager.command.commands.general;

import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import dev.yonel.wireguardbot.common.context.UserSessionContext;
import dev.yonel.wireguardbot.common.dtos.telegram.Button;
import dev.yonel.wireguardbot.common.dtos.telegram.MessageBody;
import dev.yonel.wireguardbot.common.dtos.telegram.ResponseBody;
import dev.yonel.wireguardbot.common.enums.TypeCustomButton;
import dev.yonel.wireguardbot.common.enums.TypeParseMode;
import dev.yonel.wireguardbot.common.utils.HTMLMessageBuilder;
import dev.yonel.wireguardbot.message_manager.command.CommandBase;
import dev.yonel.wireguardbot.message_manager.command.interfaces.GeneralCommandInterface;

@Lazy
@Component
public class MenuCommand extends CommandBase implements GeneralCommandInterface {

    @Override
    public List<ResponseBody> execute(MessageBody message, UserSessionContext context) throws Throwable {
        initialize();

        HTMLMessageBuilder htmlBuilder = new HTMLMessageBuilder();
        htmlBuilder.addCenteredTitle("✨ *MENÚ PRINCIPAL* ✨");
        createNewResponse(message, htmlBuilder.build());
        getCurrentResponse().setParseMode(TypeParseMode.HTML);
        getCurrentResponse().setButtons(buildMenuButtons());
        return getResponses();
    }

    @Override
    public String getName() {
        return "menu";
    }

    @Override
    public String[] getAliases() {
        return new String[] {
                "/menu",
                "menu",
                "muestrame el menu"
        };
    }

    private List<Button> buildMenuButtons() {
        return List.of(
                Button.builder()
                        .callbackData("gestion")
                        .text("🌐 Configuración")
                        .typeButton(TypeCustomButton.CALLBACKDATA)
                        .build(),
                Button.builder()
                        .callbackData("pago")
                        .text("💸 Suscripción")
                        .typeButton(TypeCustomButton.CALLBACKDATA)
                        .build(),
                Button.builder()
                        .callbackData("estadisticas")
                        .text("📈 Estatus")
                        .typeButton(TypeCustomButton.CALLBACKDATA)
                        .build(),
                Button.builder()
                        .callbackData("referidos")
                        .text("🤝 Referidos")
                        .typeButton(TypeCustomButton.CALLBACKDATA)
                        .build(),
                Button.builder()
                        .callbackData("ayuda")
                        .text("❓ Soporte")
                        .typeButton(TypeCustomButton.CALLBACKDATA)
                        .build());
    }
}
