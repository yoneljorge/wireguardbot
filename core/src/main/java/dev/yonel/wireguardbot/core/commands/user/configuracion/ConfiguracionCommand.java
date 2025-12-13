package dev.yonel.wireguardbot.core.commands.user.configuracion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private ConfiguracionCommandFactory commandFactory;

    public static final String EJECUCION_TERMINADA = "ejecucion_terminada";
    public static final String NAME = "configuracion";
    public static final String[] ALIASES =  { "/menu_configuracion", "menu configuracion", "getionar perfiles"};

    private final String OPCION_ATRAS = "/atras";
    private final String[] ATRAS_ALIASES = { "atras", "/atras", "🔙", "atras" };
    private final String OPCION_CREAR = CrearConfiguracionCommand.NAME;
    private final String[] CREAR_ALIASES = CrearConfiguracionCommand.ALIASES;
    private final String OPCION_OBTENER = ObtenerConfiguracionCommand.NAME;
    private final String[] OBTENER_ALIASES = ObtenerConfiguracionCommand.ALIASES;
    private final String OPCION_ELIMINAR = EliminarConfiguracionCommand.NAME;
    private final String[] ELIMINAR_ALIASES = EliminarConfiguracionCommand.ALIASES;

    @Override
    public List<ResponseBody> execute(MessageBody messageBody, UserSessionContext context) throws Throwable {
        initialize();

        String option = null;

        if (Match.findMaxSimilarity(messageBody.getMessage(), ATRAS_ALIASES) > 0.80) {
            context.getBotSession(messageBody.getTypeBot()).cleanData();
            context.getBotSession(messageBody.getTypeBot()).resetStep();
            context.getBotSession(messageBody.getTypeBot()).putData(SessionKey.CONFIGURACION_COMMAND_OPCION, OPCION_ATRAS);
        }

        if (context.getBotSession(messageBody.getTypeBot()).getActiveFlow().isBlank()) {
            buildMenu(messageBody);
            context.getBotSession(messageBody.getTypeBot()).setActiveFlow(getName());
        } else {
            if (context.getBotSession(messageBody.getTypeBot()).getData(SessionKey.CONFIGURACION_COMMAND_OPCION) != null) {
                option = context.getBotSession(messageBody.getTypeBot()).getData(SessionKey.CONFIGURACION_COMMAND_OPCION);
                if (option.isBlank() || option.equals(EJECUCION_TERMINADA)) {
                    option = option(messageBody.getMessage());
                    if (option != null) {
                        context.getBotSession(messageBody.getTypeBot()).putData(SessionKey.CONFIGURACION_COMMAND_OPCION,
                                option);
                    }
                }
            } else {
                option = option(messageBody.getMessage());
                if (option != null) {
                    context.getBotSession(messageBody.getTypeBot()).putData(SessionKey.CONFIGURACION_COMMAND_OPCION, option);
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

                case OPCION_OBTENER -> {
                    addResponses(commandFactory.getCommand(ObtenerConfiguracionCommand.NAME).execute(messageBody, context));
                    context.getBotSession(messageBody.getTypeBot()).reset();
                    break;
                }

                case OPCION_CREAR -> {
                    addResponses(commandFactory.getCommand(CrearConfiguracionCommand.NAME).execute(messageBody, context));
                    context.getBotSession(messageBody.getTypeBot()).reset();
                    break;
                }

                case OPCION_ELIMINAR -> {
                    addResponses(commandFactory.getCommand(EliminarConfiguracionCommand.NAME).execute(messageBody, context));
                    break;
                }

                default -> {
                    createNewResponse(messageBody, NoEntendiMessages.noEntendi());
                    getCurrentResponse().setRemovable(true);
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
        return ALIASES;
    }

    private void buildMenu(MessageBody messageBody) {
        HTMLMessageBuilder htmlMessageBuilder = new HTMLMessageBuilder();
        htmlMessageBuilder.addCenteredTitle("🌐 Configuración");
        createNewResponse(messageBody, htmlMessageBuilder.build());
        getCurrentResponse().setParseMode(TypeParseMode.HTML);
        List<Button> buttons = List.of(
                Button.builder()
                        .callbackData(OPCION_CREAR)
                        .text("✨ Crear")
                        .typeButton(TypeCustomButton.CALLBACKDATA)
                        .build(),
                Button.builder()
                        .callbackData(OPCION_OBTENER)
                        .text("📄 Obtener")
                        .typeButton(TypeCustomButton.CALLBACKDATA)
                        .build(),
                Button.builder()
                        .callbackData(OPCION_ATRAS)
                        .text("🔙 Atras")
                        .typeButton(TypeCustomButton.CALLBACKDATA)
                        .build());
        getCurrentResponse().setButtons(buttons);
        getCurrentResponse().setRemovable(true);
        getCurrentResponse().setRows(1);
    }

    private String option(String text) {
        List<Map<String, String[]>> opciones = new ArrayList<>();

        Map<String, String[]> mapCrear = new HashMap<>();
        mapCrear.put(OPCION_CREAR, CREAR_ALIASES);
        opciones.add(mapCrear);

        Map<String, String[]> mapObtener = new HashMap<>();
        mapObtener.put(OPCION_OBTENER, OBTENER_ALIASES);
        opciones.add(mapObtener);

        Map<String, String[]> mapEliminar = new HashMap<>();
        mapEliminar.put(OPCION_ELIMINAR, ELIMINAR_ALIASES);
        opciones.add(mapEliminar);

        String option = Match.findBestMatch(text, opciones);
        if (option == null) {
            option = "";
        }
        return option;
    }

}
