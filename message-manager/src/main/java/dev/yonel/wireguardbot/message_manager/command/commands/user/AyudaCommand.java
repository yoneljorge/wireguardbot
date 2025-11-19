
package dev.yonel.wireguardbot.message_manager.command.commands.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import dev.yonel.wireguardbot.common.context.UserSessionContext;
import dev.yonel.wireguardbot.common.dtos.telegram.MessageBody;
import dev.yonel.wireguardbot.common.dtos.telegram.ResponseBody;
import dev.yonel.wireguardbot.common.enums.TypeParseMode;
import dev.yonel.wireguardbot.common.utils.Match;
import dev.yonel.wireguardbot.message_manager.command.Command;
import dev.yonel.wireguardbot.message_manager.command.CommandBase;
import dev.yonel.wireguardbot.message_manager.messages.AyudaMessage;
import dev.yonel.wireguardbot.message_manager.messages.NoEntendiMessages;

@Lazy
@Component
public class AyudaCommand extends CommandBase implements Command {

    private List<Map<String, String[]>> opciones = new ArrayList<>();

    public AyudaCommand() {

        Map<String, String[]> ayuda = new HashMap<>();
        ayuda.put("0", new String[] {
                "0",
                "ayuda",
                "muestrame la ayuda"
        });
        opciones.add(ayuda);

        Map<String, String[]> comoJugar = new HashMap<>();
        comoJugar.put("1", new String[] {
                "1",
                "como ",
                "que tengo que hacer "
        });
        opciones.add(comoJugar);
    }

    @Override
    public List<ResponseBody> execute(MessageBody message, UserSessionContext context) throws Throwable {
        context.getBotSession(message.getTypeBot()).setActiveFlow("ayuda");

        // Cargamos los datos en la clase padre
        initialize();
        String input = message.getMessage();

        String opcion = Optional.ofNullable(Match.findBestMatch(input, opciones))
                .orElse("");

        switch (opcion) {
            case "0" -> {
                step0(message);
                break;
            }

            case "1" -> {
                comoJugar(message, context);
                break;
            }

            default -> {
                defaultMessage(message, context);
                break;
            }
        }

        return getResponses();
    }

    @Override
    public String getName() {
        return "ayuda";
    }

    @Override
    public String[] getAliases() {
        return new String[] {
                "/ayuda",
                "ayuda",
                "como hago ",
                "como",
                "tengo dudas",
                "que tengo que hacer"
        };
    }

    private void step0(MessageBody messageBody) {
        // Enviamos las opciones como botones
        // response.setButtons(buildButtons());
        createNewResponse(messageBody, """
                ❓ Soporte.\n
                Contactar a @yoneljorge
                """);
    }

    private void comoJugar(MessageBody messageBody, UserSessionContext context) {
        /*
         * Reiniciamos el contexto luego de mostrarle la ayuda
         */
        context.getBotSession(messageBody.getTypeBot()).reset();
        createNewResponse(messageBody, AyudaMessage.message());
        getCurrentResponse().setParseMode(TypeParseMode.HTML);
    }

    private void defaultMessage(MessageBody messageBody, UserSessionContext context) {
        // En caso de que el mensaje que el usuario envie no encuentre una respuesta se
        // le manda el mensaje de que no se entiende lo que escribio.
        createNewResponse(messageBody, NoEntendiMessages.noEntendi());
        getCurrentResponse().setParseMode(TypeParseMode.MARKDOWNV2);
    }
}
