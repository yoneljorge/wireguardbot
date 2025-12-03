
package dev.yonel.wireguardbot.message_manager.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import dev.yonel.wireguardbot.common.context.SessionManager;
import dev.yonel.wireguardbot.common.context.UserSessionContext;
import dev.yonel.wireguardbot.common.dtos.UserDto;
import dev.yonel.wireguardbot.common.dtos.telegram.MessageBody;
import dev.yonel.wireguardbot.common.dtos.telegram.ResponseBody;
import dev.yonel.wireguardbot.common.enums.TypeSendExecution;
import dev.yonel.wireguardbot.common.enums.TypeWebhookTelegramBot;
import dev.yonel.wireguardbot.common.events.MessageRelayToDeleteMessageEvent;
import dev.yonel.wireguardbot.common.services.database.UserService;
import dev.yonel.wireguardbot.common.services.message_manager.MessageRelayService;
import dev.yonel.wireguardbot.message_manager.command.interfaces.Command;
import dev.yonel.wireguardbot.message_manager.command.registry.GeneralCommandRegistry;
import dev.yonel.wireguardbot.message_manager.command.utils.CommandDetector;
import lombok.extern.slf4j.Slf4j;

@Lazy
@Slf4j
@Service
public class MessageRelayServiceImpl implements MessageRelayService {

    @Autowired
    private UserService usuarioService;
    @Autowired
    private CommandDetector commandDetector;
    @Autowired
    private GeneralCommandRegistry generalCommandRegistry;
    @Autowired
    private ApplicationEventPublisher publisher;

    /**
     * Manejador global de mensajes.
     */
    public List<ResponseBody> handleMessage(MessageBody messageBody) throws Throwable {
        UserSessionContext context = SessionManager.getContext(messageBody.getUserid());

        /*
         * En caso de que existan mensajes por eliminar entonces se ejecuta este evento
         * para eliminar el mensaje.
         */
        if (context.getBotSession(messageBody.getTypeBot()).isPendingMessageToDelete()) {
            for (int messageId : context.getBotSession(messageBody.getTypeBot()).getMessagesIdToDelete()) {
                publisher.publishEvent(new MessageRelayToDeleteMessageEvent(this, messageBody.getUserid(), messageId));
            }
        }

        if (messageBody.isGroup()) {
            return handleGroupMessage(messageBody);
        }
        return handleUserMessage(messageBody);
    }

    /**
     * Se manejan los mensajes de los grupos.
     * 
     * @param messageBody
     * @return
     */
    private List<ResponseBody> handleGroupMessage(MessageBody messageBody) {
        log.info("Mensaje recibido desde grupo");
        // FIXME: Implementar lógica para mensajes de grupo
        return null;
    }

    private List<ResponseBody> handleUserMessage(MessageBody messageBody) throws Throwable {
        UserSessionContext context = SessionManager.getContext(messageBody.getUserid());

        /*
         * En caso de que el mensaje sea el de un nuevo usuario entonces se ejecuta
         * startCommnad para registrar al nuevo usuario.
         */
        if (messageBody.getTypeBot() != TypeWebhookTelegramBot.ADMIN && !isUserRegistered(messageBody.getUserid())) {
            return handleNewUser(messageBody, context);
        }

        /*
         * Handle para usuarios que ya estan registrados.
         */
        return handleRegisteredUser(messageBody, context);
    }

    private boolean isUserRegistered(Long userId) throws Throwable {
        if (usuarioService.getUserByUserId(userId).isPresent()) {
            return true;
        } else {
            return false;
        }
    }

    private List<ResponseBody> handleNewUser(MessageBody messageBody, UserSessionContext context) throws Throwable {
        Command startCommand = generalCommandRegistry.getCommands().stream()
                .filter(command -> command.getName().equals("start"))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("StartCommand not found"));
        return startCommand.execute(messageBody, context);
    }

    private List<ResponseBody> handleRegisteredUser(MessageBody messageBody, UserSessionContext context)
            throws Throwable {
        /*
         * Si el usuario cambia sus datos entonces los actualizamos en la base de datos.
         */
        if (isUserUpdateInformation(messageBody)) {
            Optional<UserDto> userOptional = buildUserFromNewInformation(messageBody);
            if (userOptional.isPresent()) {
                try {
                    usuarioService.updateUser(userOptional.get());
                } catch (Exception e) {
                    log.error("Error en MessageRelayService.handleRegisteredUser: {}", e.getMessage());
                }
            } else {
                log.error("Error actualizando al usuario. No se pudo construir el usuario actualizado.");
            }
        }
        processResetCommands(messageBody, context);

        return handleCommandExecution(messageBody, context);
    }

    private void processResetCommands(MessageBody messageBody, UserSessionContext context) throws Throwable {
        String message = messageBody.getMessage() != null ? messageBody.getMessage().toLowerCase() : "";
        String callbackData = messageBody.getCallbackData() != null ? messageBody.getCallbackData().toLowerCase() : "";

        if (callbackData.toLowerCase().startsWith("reset")) {
            context.getBotSession(messageBody.getTypeBot()).reset();
            messageBody.setCallbackData(callbackData.substring(5));
        }

        if (message.toLowerCase().startsWith("reset") || message.toLowerCase().startsWith("ayuda")) {
            context.getBotSession(messageBody.getTypeBot()).reset();
        }
    }

    private List<ResponseBody> handleCommandExecution(MessageBody messageBody, UserSessionContext context)
            throws Throwable {
        try {
            Command command = commandDetector.findBestMatch(messageBody, context);
            if (command != null) {
                return command.execute(messageBody, context);
            }
        } catch (Exception e) {
            log.error("Error ejecutando comando: {}", e.getMessage());
            throw e;
        }

        return buildDefaultResponse(messageBody);
    }

    private List<ResponseBody> buildDefaultResponse(MessageBody messageBody) {
        return List.of(ResponseBody.builder()
                .messageId(messageBody.getMessageId())
                .chatid(messageBody.getChatid())
                .userid(messageBody.getUserid())
                .typeSendExecution(TypeSendExecution.SEND_MESSAGE)
                .response("No entendí tu mensaje 🤔 ¿Puedes reformularlo?")
                .build());

    }

    private boolean isUserUpdateInformation(MessageBody messageBody) throws Throwable {
        Optional<UserDto> usuarioOptional = usuarioService.getUserByUserId(messageBody.getUserid());
        if (usuarioOptional.isPresent()) {
            UserDto usuario = usuarioOptional.get();
            if (!usuario.getUserName().equals(messageBody.getUserName())) {
                return true;
            }

            if (!usuario.getFirstName().equals(messageBody.getFirstName())) {
                return true;
            }

            if (!usuario.getLastName().equals(messageBody.getLastName())) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }

    }

    private Optional<UserDto> buildUserFromNewInformation(MessageBody messageBody) throws Throwable {
        Optional<UserDto> userOptional = usuarioService.getUserByUserId(messageBody.getUserid());

        if (userOptional.isPresent()) {
            UserDto user = userOptional.get();
            try {
                if (!messageBody.getFirstName().isBlank()) {
                    user.setFirstName(messageBody.getFirstName());
                }

                if (!messageBody.getLastName().isBlank()) {
                    user.setLastName(messageBody.getLastName());
                }

                if (!messageBody.getUserName().isBlank()) {
                    user.setUserName(messageBody.getUserName());
                }

                return Optional.ofNullable(user);
            } catch (Exception e) {
                log.error("Error in MessageRelayService.buildUserFromNewInformation: {}", e.getMessage());
                throw new Throwable();
            }
        } else {
            return Optional.empty();
        }
    }
}