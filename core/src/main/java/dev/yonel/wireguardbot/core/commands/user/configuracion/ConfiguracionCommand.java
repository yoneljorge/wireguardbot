package dev.yonel.wireguardbot.core.commands.user.configuracion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import dev.yonel.wireguardbot.common.context.SessionKey;
import dev.yonel.wireguardbot.common.context.UserSessionContext;
import dev.yonel.wireguardbot.common.dtos.telegram.Button;
import dev.yonel.wireguardbot.common.dtos.telegram.MessageBody;
import dev.yonel.wireguardbot.common.dtos.telegram.ResponseBody;
import dev.yonel.wireguardbot.common.enums.TypeCustomButton;
import dev.yonel.wireguardbot.common.enums.TypeParseMode;
import dev.yonel.wireguardbot.common.utils.HTMLMessageBuilder;
import dev.yonel.wireguardbot.common.utils.Match;
import dev.yonel.wireguardbot.message_manager.command.CommandBase;
import dev.yonel.wireguardbot.message_manager.command.interfaces.UserCommandInterface;
import dev.yonel.wireguardbot.message_manager.messages.NoEntendiMessages;

@Component
public class ConfiguracionCommand extends CommandBase implements UserCommandInterface {

    public static final String EJECUCION_TERMINADA = "ejecucion_terminada";
    public static final String NAME = "configuracion";

    private final String OPCION_ATRAS = "atras";
    private final String OPCION_GESTION = "gestion";
    private final String OPCION_CREAR = "crear";
    private final String OPCION_OBTENER = "obtener";
    private final String OPCION_ELIMINAR = "eliminar";

    @Override
    public List<ResponseBody> execute(MessageBody messageBody, UserSessionContext context) throws Throwable {
        initialize();

        String option = null;

        String[] listAtras = new String[] { "atras", "/atras", "🔙", "atras" };
        if (Match.findMaxSimilarity(messageBody.getMessage(), listAtras) > 0.80) {
            context.getBotSession(messageBody.getTypeBot()).cleanData();
            context.getBotSession(messageBody.getTypeBot()).resetStep();
            context.getBotSession(messageBody.getTypeBot()).putData(SessionKey.GESTION_COMMAND_OPCION, OPCION_ATRAS);
        }

        if (context.getBotSession(messageBody.getTypeBot()).getActiveFlow().isBlank()) {
            buildMenu(messageBody);
            context.getBotSession(messageBody.getTypeBot()).setActiveFlow(getName());
        } else {
            if (context.getBotSession(messageBody.getTypeBot()).getData(SessionKey.GESTION_COMMAND_OPCION) != null) {
                option = context.getBotSession(messageBody.getTypeBot()).getData(SessionKey.GESTION_COMMAND_OPCION);
                if (option.isBlank() || option.equals(EJECUCION_TERMINADA)) {
                    option = option(messageBody.getMessage());
                    if (option != null) {
                        context.getBotSession(messageBody.getTypeBot()).putData(SessionKey.GESTION_COMMAND_OPCION,
                                option);
                    }
                }
            } else {
                option = option(messageBody.getMessage());
                if (option != null) {
                    context.getBotSession(messageBody.getTypeBot()).putData(SessionKey.GESTION_COMMAND_OPCION, option);
                }
            }

            /*
             * Evitamos un potencial null.
             */
            if (option == null) {
                option = "";
            }

            switch (option) {
                case OPCION_ATRAS -> {
                    buildMenu(messageBody);
                    context.getBotSession(messageBody.getTypeBot()).reset();
                    context.getBotSession(messageBody.getTypeBot()).setActiveFlow(getName());
                    break;
                }

                case OPCION_GESTION -> {
                    addResponses(ConfiguracionCommandFactory.getCommand(GestionarConfiguracionCommand.NAME).execute(messageBody, context));
                    break;
                }

                case OPCION_OBTENER -> {
                    addResponses(ConfiguracionCommandFactory.getCommand(ObtenerConfiguracionCommand.NAME).execute(messageBody, context));
                    break;
                }

                case OPCION_CREAR -> {
                    addResponses(ConfiguracionCommandFactory.getCommand(CrearConfiguracionCommand.NAME).execute(messageBody, context));
                    break;
                }

                case OPCION_ELIMINAR -> {
                    addResponses(ConfiguracionCommandFactory.getCommand(EliminarConfiguracionCommand.NAME).execute(messageBody, context));
                    break;
                }

                default -> {
                    createNewResponse(messageBody, NoEntendiMessages.noEntendi());
                    break;
                }
            }
        }
        return getResponses();
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String[] getAliases() {
        return new String[] { "conf", "configuracion", "gestionar conf", "crear_conf", "obtener_conf", "eliminar_conf" };
    }

    private void buildMenu(MessageBody messageBody) {
        HTMLMessageBuilder htmlMessageBuilder = new HTMLMessageBuilder();
        htmlMessageBuilder.addCenteredTitle("🌐 Configuración");
        createNewResponse(messageBody, htmlMessageBuilder.build());
        getCurrentResponse().setParseMode(TypeParseMode.HTML);
        List<Button> buttons = List.of(
                Button.builder()
                        .callbackData("gestion")
                        .text("⚙️ Gestion")
                        .typeButton(TypeCustomButton.CALLBACKDATA)
                        .build(),
                Button.builder()
                        .callbackData("crear")
                        .text("✨ Crear Configuración")
                        .typeButton(TypeCustomButton.CALLBACKDATA)
                        .build(),
                Button.builder()
                        .callbackData("obtener")
                        .text("📄 Obtener Configuración")
                        .typeButton(TypeCustomButton.CALLBACKDATA)
                        .build(),
                Button.builder()
                        .callbackData("eliminar")
                        .text("🗑️ Eliminar")
                        .typeButton(TypeCustomButton.CALLBACKDATA)
                        .build(),
                Button.builder()
                        .callbackData("atras")
                        .text("🔙 Atras")
                        .typeButton(TypeCustomButton.CALLBACKDATA)
                        .build());
        getCurrentResponse().setButtons(buttons);
    }

    private String option(String text) {
        List<Map<String, String[]>> opciones = new ArrayList<>();

        Map<String, String[]> mapGestion = new HashMap<>();
        mapGestion.put(OPCION_GESTION, new String[] {"gestion", "gestionar configuracion"});
        opciones.add(mapGestion);

        Map<String, String[]> mapCrear = new HashMap<>();
        mapCrear.put(OPCION_CREAR, new String[] { "crear", "crear configuracion" });
        opciones.add(mapCrear);

        Map<String, String[]> mapObtener = new HashMap<>();
        mapObtener.put(OPCION_OBTENER, new String[] { "obtener", "obtener configuracion" });
        opciones.add(mapObtener);

        Map<String, String[]> mapEliminar = new HashMap<>();
        mapEliminar.put(OPCION_ELIMINAR, new String[] { "eliminar", "eliminar configuracion" });
        opciones.add(mapEliminar);

        String option = Match.findBestMatch(text, opciones);
        if (option == null) {
            option = "";
        }
        return option;
    }

}
