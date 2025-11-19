package dev.yonel.wireguardbot.message_manager.command;

import java.util.Optional;

import org.simmetrics.StringMetric;
import org.simmetrics.metrics.StringMetrics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import dev.yonel.wireguardbot.common.context.UserSessionContext;
import dev.yonel.wireguardbot.common.dtos.UserDto;
import dev.yonel.wireguardbot.common.dtos.telegram.MessageBody;
import dev.yonel.wireguardbot.common.enums.TypeRol;
import dev.yonel.wireguardbot.common.enums.TypeWebhookTelegramBot;
import dev.yonel.wireguardbot.common.services.UserService;
import dev.yonel.wireguardbot.message_manager.command.commands.ErrorCommand;
import lombok.extern.slf4j.Slf4j;

@Lazy
@Slf4j
@Component
public class CommandDetector {

    @Autowired
    private UserService usuarioServiceClient;

    @Autowired
    private ErrorCommand errorCommand;

    private static final StringMetric metric = StringMetrics.levenshtein();
    private final CommandRegistryUser commandRegistryUser;
    private final CommandRegistryAdmin commandRegistryAdmin;
    private final CommandRegistryComercial commandRegistryComercial;
    private final CommandRegistryGeneral commandRegistryGeneral;

    public CommandDetector(CommandRegistryUser commandRegistryUser, CommandRegistryAdmin commandRegistryAdmin,
            CommandRegistryComercial commandRegistryComercial, CommandRegistryGeneral commandRegistryGeneral) {
        this.commandRegistryUser = commandRegistryUser;
        this.commandRegistryAdmin = commandRegistryAdmin;
        this.commandRegistryComercial = commandRegistryComercial;
        this.commandRegistryGeneral = commandRegistryGeneral;
    }

    public Command findBestMatch(MessageBody messageBody, UserSessionContext context) {
        final String inputTrim;

        try {
            if (messageBody.getMessage().toLowerCase().startsWith("salir")) {
                inputTrim = messageBody.getMessage();
            } else if (!context.getBotSession(messageBody.getTypeBot()).getActiveFlow().isBlank()) {
                inputTrim = context.getBotSession(messageBody.getTypeBot()).getActiveFlow().toLowerCase().trim();
            } else {
                inputTrim = messageBody.getMessage().toLowerCase().trim();
            }

            Command bestMatch = null;
            double maxSimilarity = 0.0;

            /*
             * Obtenemos el usuario
             */
            Optional<UserDto> usuarioOptional = usuarioServiceClient.getUserByUserId(messageBody.getUserid());
            UserDto usuario;
            if(usuarioOptional.isPresent()){
                usuario = usuarioOptional.get();
            }else{
                throw new InternalError();
            }
            try {
                /*
                 * Verificamos si la peticion viene del bot admin y si el usuario es
                 * administrador
                 */
                if (messageBody.getTypeBot() == TypeWebhookTelegramBot.ADMIN && usuario.getTypeRol() == TypeRol.ADMIN) {
                    for (Command command : commandRegistryAdmin.getCommands()) {
                        if (command.getName().equals(inputTrim)) {
                            bestMatch = command;
                            break;
                        }
                        double similarity = getMaxSimilarity(inputTrim, command.getAliases());
                        if (similarity > maxSimilarity && similarity > 0.65) {
                            maxSimilarity = similarity;
                            bestMatch = command;
                        }
                    }
                }

                /*
                 * Verificamos si la petición viene del bot recogedor y el usuario es comercial o administrador.
                 */
                if ((usuario.getTypeRol() == TypeRol.COMERCIAL || usuario.getTypeRol() == TypeRol.ADMIN)
                        && messageBody.getTypeBot() == TypeWebhookTelegramBot.CLIENT) {
                    for (Command command : commandRegistryComercial.getCommands()) {
                        if (command.getName().equals(inputTrim)) {
                            bestMatch = command;
                            break;
                        }
                        double similarity = getMaxSimilarity(inputTrim, command.getAliases());
                        if (similarity > maxSimilarity && similarity > 0.65) {
                            maxSimilarity = similarity;
                            bestMatch = command;
                        }
                    }
                }
            } catch (Throwable e) {
                log.info("Usuario no encontrado: {}", e.getMessage());
            }


            /*
             * Solo recibimos peticiones del bot recogedor
             */
            if (messageBody.getTypeBot() == TypeWebhookTelegramBot.CLIENT) {
                for (Command command : commandRegistryUser.getCommands()) {
                    if (command.getName().equals(inputTrim)) {
                        bestMatch = command;
                        break;
                    }
                    double similarity = getMaxSimilarity(inputTrim, command.getAliases());
                    if (similarity > maxSimilarity && similarity > 0.65) {
                        maxSimilarity = similarity;
                        bestMatch = command;
                    }
                }
            }

            for (Command command : commandRegistryGeneral.getCommands()) {
                    if (command.getName().equals(inputTrim)) {
                        bestMatch = command;
                        break;
                    }
                    double similarity = getMaxSimilarity(inputTrim, command.getAliases());
                    if (similarity > maxSimilarity && similarity > 0.65) {
                        maxSimilarity = similarity;
                        bestMatch = command;
                    }
                }

            if (bestMatch == null) {
                return commandRegistryUser.getCommands().getFirst();
            }

            return bestMatch;
        } catch (Exception e) {
            log.error("Error findBestMatch - CommandDetector: ", e.getMessage());
            return errorCommand;
        }
    }

    public Command findBestMatch(String name) {
        try {
            final String inputTrim = name.toLowerCase().trim();
            Command bestMatch = null;
            double maxSimilarity = 0.0;

            for (Command command : commandRegistryUser.getCommands()) {
                double similarity = getMaxSimilarity(inputTrim, command.getAliases());
                if (similarity > maxSimilarity && similarity > 0.65) {
                    maxSimilarity = similarity;
                    bestMatch = command;
                }
            }

            if (bestMatch == null) {
                return commandRegistryUser.getCommands().getFirst();
            }

            return bestMatch;
        } catch (Exception e) {
            log.error("Error findBestMatch - CommandDetector: ", e.getMessage());
            return errorCommand;
        }
    }

    private static double getMaxSimilarity(String input, String[] aliases) {
        double max = 0.0;
        for (String alias : aliases) {
            double score = metric.compare(input, alias.toLowerCase());
            if (score > max)
                max = score;
        }
        return max;
    }

    @SuppressWarnings("unused")
    private record MatchResult(Command command, double similarity) {
    }
}
