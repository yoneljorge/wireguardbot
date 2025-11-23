
package dev.yonel.wireguardbot.message_manager.command.commands.general;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import dev.yonel.wireguardbot.common.context.SessionKey;
import dev.yonel.wireguardbot.common.context.UserSessionContext;
import dev.yonel.wireguardbot.common.dtos.UserDto;
import dev.yonel.wireguardbot.common.dtos.telegram.MessageBody;
import dev.yonel.wireguardbot.common.dtos.telegram.ResponseBody;
import dev.yonel.wireguardbot.common.enums.TypeParseMode;
import dev.yonel.wireguardbot.common.enums.TypeWebhookTelegramBot;
import dev.yonel.wireguardbot.common.events.MessageRelayToTelegramBotClientEvent;
import dev.yonel.wireguardbot.common.events.SendToStartCommandEvent;
import dev.yonel.wireguardbot.common.services.database.UserService;
import dev.yonel.wireguardbot.message_manager.command.CommandBase;
import dev.yonel.wireguardbot.message_manager.command.interfaces.GeneralCommandInterface;
import dev.yonel.wireguardbot.message_manager.messages.BienvenidaMessage;
import dev.yonel.wireguardbot.message_manager.messages.ErrorMessage;
import dev.yonel.wireguardbot.message_manager.messages.StartMessages;
import lombok.extern.slf4j.Slf4j;

@Lazy
@Slf4j
@Component
public class StartCommand extends CommandBase implements GeneralCommandInterface {

    @Autowired
    private UserService usuarioService;

    @Autowired
    private ApplicationEventPublisher publisher;

    @Override
    public String getName() {
        return "start";
    }

    @Override
    public String[] getAliases() {
        return new String[] {
                "/start",
                "start",
                "quiero unirme",
                "quiero entrar a la loteria",
                "inscribeme",
                "me anoto"
        };
    }

    @Override
    public List<ResponseBody> execute(MessageBody message, UserSessionContext context) throws Throwable {

        initialize();

        switch (context.getBotSession(message.getTypeBot()).getStep()) {
            case 0 -> {
                step0(message, context);
                break;
            }

            case 1 -> {
                step1(message, context);
                break;
            }
            default -> {
                break;
            }

        }

        return getResponses();
    }

    @EventListener
    public void handleSendToStartCommandEvent(SendToStartCommandEvent event) throws Throwable {
        MessageBody message = event.getMessageBody();
        UserSessionContext context = (UserSessionContext) event.getContext();

        initialize();

        switch (context.getBotSession(message.getTypeBot()).getStep()) {
            case 0 -> {
                step0(message, context);
                break;
            }

            case 1 -> {
                step1(message, context);
                break;
            }
            default -> {
                break;
            }
        }
        publisher.publishEvent(new MessageRelayToTelegramBotClientEvent(this, getResponses()));
    }

    @SuppressWarnings("null")
    private void step0(MessageBody messageBody, UserSessionContext context) {

        String username = messageBody.getAlias();
        Long userid = messageBody.getUserid();

        try {
            if (!isRegisteredUsuario(userid)) {
                /*
                 * Si no es un usuario registrado, entonces se procede a registrar.
                 */
                try {
                    /*
                     * Si no tiene un nombre de usuario se le pide uno.
                     */
                    if (username == null && username.trim().equals("")) {
                        context.getBotSession(messageBody.getTypeBot()).setActiveFlow("start");
                        context.getBotSession(messageBody.getTypeBot()).putData(SessionKey.COMMAND_START_ESPERAR_USERNAME, "si");
                        context.getBotSession(messageBody.getTypeBot()).nextStep();

                        solicitarNombreDeUsuario(messageBody);

                        return;
                    } else {
                        /*
                         * Si el usuario tiene username pasamos al paso 1.
                         */
                        context.getBotSession(messageBody.getTypeBot()).nextStep();
                        context.getBotSession(messageBody.getTypeBot()).putData(SessionKey.COMMAND_START_ESPERAR_USERNAME, "no");
                        step1(messageBody, context);
                    }
                } catch (Exception e) {
                    context.getBotSession(messageBody.getTypeBot()).setActiveFlow("start");
                    context.getBotSession(messageBody.getTypeBot()).putData(SessionKey.COMMAND_START_ESPERAR_USERNAME, "si");
                    context.getBotSession(messageBody.getTypeBot()).nextStep();

                    solicitarNombreDeUsuario(messageBody);

                    return;
                }
            } else {
                /*
                 * Si el mensaje proviene de un usuario registrado es que necesita ayuda o el
                 * menu, por lo que se lo vamos a proporcionar en el mensaje.
                 */
                /*
                 * Si el mensaje proviene del bot administrador entonces le decimos que solo se
                 * responden mensajes que tengan que ver con la administración.
                 */
                if (messageBody.getTypeBot() == TypeWebhookTelegramBot.CLIENT) {
                    createNewResponse(messageBody, StartMessages.getMessageDefault());
                    getCurrentResponse().setParseMode(TypeParseMode.HTML);
                    getCurrentResponse().setRemovable(true);
                } else if (messageBody.getTypeBot() == TypeWebhookTelegramBot.ADMIN) {
                    createNewResponse(messageBody,
                            "Este bot es solo para administrar.\nPuede ver las opciones con el comando /menu.");
                }

                return;
            }
        } catch (Throwable e) {
            e.printStackTrace();
            log.error("Error en paso 0 en StartCommand: {}", e.getMessage());
            createNewResponse(messageBody, ErrorMessage.getMessage());
            return;
        }

    }

    /*
     * Método con el cual se termina el registro del usuario.
     */
    private void step1(MessageBody messageBody, UserSessionContext context) throws Throwable {
        String esperarUsername = (String) context.getBotSession(messageBody.getTypeBot()).getData(SessionKey.COMMAND_START_ESPERAR_USERNAME);
        String username = null;

        if (esperarUsername.equals("si")) {
            username = messageBody.getMessage();
        } else {
            username = messageBody.getAlias();
        }
            log.info("Creando el nuevo usuario.");

            // Guardando usuario
            try {
                UserDto usuario = new UserDto(messageBody);
                usuario.setUserName(username);
                usuarioService.createUser(usuario);
                mensajeBienvenidaPrivado(messageBody, username);
                context.getBotSession(messageBody.getTypeBot()).reset();
            } catch (Throwable e) {
                e.printStackTrace();
                log.error("Error guardando usuario: {}", e.getMessage());
                throw new Throwable("Error guardando usuario: " + e.getMessage());
            }
            return;
        
    }

    /**
     * Método mediante el cual verificamos si el usuario está registrado.
     * 
     * @param userid
     *               el identificador del usuario.
     * @return {@code true} en caso de que exista; {@code false} en caso de que no
     *         exista.
     * @throws Throwable
     */
    private boolean isRegisteredUsuario(Long userid) throws Throwable {
        try {
            Optional<UserDto> gettedUser = usuarioService.getUserByUserId(userid);
            if (gettedUser.isPresent()) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }

    }


    private void solicitarNombreDeUsuario(MessageBody messageBody) {
        createNewResponse(messageBody, BienvenidaMessage.mensajeParaContinuarActualiceUsername());
        getCurrentResponse().setParseMode(TypeParseMode.MARKDOWNV2);
    }

    private void mensajeBienvenidaPrivado(MessageBody messageBody, String username) {
        createNewResponse(messageBody, BienvenidaMessage.message(username));
        getCurrentResponse().setParseMode(TypeParseMode.HTML);
    }
}
