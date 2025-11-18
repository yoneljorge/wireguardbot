package dev.yonel.wireguardbot.bot.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import dev.yonel.wireguardbot.bot.components.EmptyResponse;
import dev.yonel.wireguardbot.bot.components.TelegramBotCustomOptions;
import dev.yonel.wireguardbot.bot.components.TelegramPlatform;
import dev.yonel.wireguardbot.bot.components.custom.CustomEditMessageReplyMarkup;
import dev.yonel.wireguardbot.bot.components.custom.CustomEditMessageText;
import dev.yonel.wireguardbot.bot.components.custom.CustomSendMessage;
import dev.yonel.wireguardbot.common.context.SessionManager;
import dev.yonel.wireguardbot.common.context.UserSessionContext;
import dev.yonel.wireguardbot.common.dtos.telegram.ResponseBody;
import dev.yonel.wireguardbot.common.enums.TypeWebhookTelegramBot;
import dev.yonel.wireguardbot.common.events.MessageRelayToDeleteMessageEvent;
import dev.yonel.wireguardbot.common.properties.SchedulerAutoSendMessageProperties;
import dev.yonel.wireguardbot.common.properties.telegram.TelegramBotAdminProperties;
import dev.yonel.wireguardbot.common.properties.telegram.TelegramBotClientProperties;
import dev.yonel.wireguardbot.common.properties.telegram.TelegramBotProperties;
import dev.yonel.wireguardbot.common.scheduler.send_auto_message.SendAutoMessageToTelegramBotClientScheduler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BaseTelegramWebhookBot extends TelegramWebhookBot implements MessageDeletionHandler {

    @Autowired
    protected TelegramPlatform telegramPlatform;

    @Autowired
    private SchedulerAutoSendMessageProperties autoSendProperties;
    @Autowired
    private TelegramBotAdminProperties telegramBotAdminProperties;
    @Autowired
    private TelegramBotClientProperties telegramBotClientProperties;

    private final String BOT_TOKEN;
    private final String BOT_USERNAME;
    private final String BOT_PATH;

    public BaseTelegramWebhookBot(TelegramBotProperties telegramBotProperties) {
        super(new TelegramBotCustomOptions(), telegramBotProperties.getToken());

        this.BOT_TOKEN = telegramBotProperties.getToken();
        this.BOT_USERNAME = telegramBotProperties.getUsername();
        this.BOT_PATH = telegramBotProperties.getPath();
    }

    @Override
    public String getBotPath() {
        return BOT_PATH;
    }

    @Override
    public String getBotUsername() {
        return BOT_USERNAME;
    }

    @Override
    public void onRegister() {
        super.onRegister();
        log.info(">>>>> Bot registrado exitosamente en Telegram <<<<<");
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        if (autoSendProperties.isActive()) {
            SendAutoMessageToTelegramBotClientScheduler.resetCount();
        }

        TypeWebhookTelegramBot bot = null;

        if (this.BOT_TOKEN.equalsIgnoreCase(telegramBotAdminProperties.getToken())) {
            bot = TypeWebhookTelegramBot.ADMIN;
        }

        if (this.BOT_TOKEN.equalsIgnoreCase(telegramBotClientProperties.getToken())) {
            bot = TypeWebhookTelegramBot.CLIENT;
        }

        if (update.hasMessage()) {
            try {
                handleMessageUpdate(update, bot);
            } catch (Throwable e) {
                log.error("Error en handleMessageUpdate: {}", e.getMessage());
            }
        } else if (update.hasCallbackQuery()) {
            handleCallbackQueryUpdate(update, bot);
        }
        return new EmptyResponse();
    }

    private void handleMessageUpdate(Update update, TypeWebhookTelegramBot bot) throws Throwable {
        Long userId = update.getMessage().getFrom().getId();
        Long chatId = update.getMessage().getChatId();
        String chatType = update.getMessage().getChat().getType();

        log.info("Mensaje recibido del chat ID: {}, tipo: {}, usuario ID: {}", chatId, chatType,
                userId);
        processPrivateMessage(update, bot);

    }

    private void processPrivateMessage(Update update, TypeWebhookTelegramBot bot) throws Throwable {

        String messageText = update.getMessage().getText();

        if (update.getMessage().hasPhoto()) {
            try {
                List<Object> response = convertToObject(telegramPlatform.receivedImageMessageFromPrivate(update,
                        getFileUrl(update), bot));
                handleResponse(response, bot);
                return;
            } catch (Exception e) {
                log.error("Error en telegramPlatform.receivedImageMessageFromPrivate: {}", e.getMessage());
            }
        }

        if (messageText != null) {
            try {
                List<Object> response = convertToObject(
                        telegramPlatform.receivedMessageFromPrivate(update, bot));
                handleResponse(response, bot);
                return;
            } catch (Throwable e) {
                log.error("Error en telegramPlatform.receivedFromMessage (privado): {}", e.getMessage(), e);
                handleResponse(null, bot);
                return;
            }
        }
    }

    private void handleCallbackQueryUpdate(Update update, TypeWebhookTelegramBot bot) {
        Long userId = update.getCallbackQuery().getFrom().getId();
        Long chatId = null;
        String chatType = null;

        if (update.getCallbackQuery().getMessage() != null) {
            Message message = (Message) update.getCallbackQuery().getMessage();
            chatId = message.getChatId();
            chatType = message.getChat().getType();
        } else if (update.getCallbackQuery().getChatInstance() != null) {
            /*
             * Esto puede indicar que el callback se originó de un mensaje inline
             * En este caso, no hay un 'message' asociado directamente
             * 
             * El 'chatInstance' podría ser útil en algunos contextos, pero no proporciona
             * el tipo de chat de la misma manera.
             */
            log.warn("CallbackQuery sin mensaje acociado directamente (chatInstance: {})",
                    update.getCallbackQuery().getChatInstance());
            /*
             * Dependiendo de la lógica, se pudiera necesitar manejar esto de forma
             * diferente.
             * Por ejemplo, podrías no tener el tipo de chat disponible aquí.
             */
            return; // No se puede obtener la información del chat
        }
        log.info("CallbackQuery recibido del chat ID: {}, tipo: {}, usuario ID: {}", chatId, chatType,
                userId);
        try {
            handleResponse(convertToObject(telegramPlatform.removeButtons(update, new ResponseBody())), bot);
            handleResponse(convertToObject(telegramPlatform.receivedFromButtons(update, bot)), bot);
        } catch (TelegramApiException e) {
            log.error("Error al procesar CallbackQuery: {}", e.getMessage(), e);
        } catch (Throwable e) {
            log.error("Error en telegramPlatform.receivedFromButtons (callback): {}", e.getMessage(), e);
        }
    }

    protected void handleResponse(List<Object> responses, TypeWebhookTelegramBot bot) {
        if (responses == null || responses.isEmpty()) {
            returnResponseEmpty();
            return;
        }

        /**
         * Lista en la cual vamos a agregar los mensajes enviados que tiene que ser
         * eliminados.
         */
        List<Message> returnedMessages = new ArrayList<>();

        for (Object response : responses) {

            switch (response) {
                case CustomSendMessage sendMessage -> {
                    try {
                        if (sendMessage.isRemovable()) {
                            log.info("marcando mensaje para eliminar");
                            Message message = execute(sendMessage);
                            returnedMessages.add(message);
                        } else {
                            execute(sendMessage);
                        }

                    } catch (TelegramApiException e) {
                        log.error("Error enviando SendMessage: {}", e.getMessage());
                    }
                }
                case CustomEditMessageText editMessageText -> {
                    try {
                        execute(editMessageText);
                    } catch (TelegramApiException e) {
                        log.error("Error enviando EditMessageText: {}", e.getMessage());
                    }
                }
                case CustomEditMessageReplyMarkup editMessageReplyMarkup -> {
                    try {
                        execute(editMessageReplyMarkup);
                    } catch (TelegramApiException e) {
                        log.error("Error enviando EditMessageReplyMarkup: {}", e.getMessage());
                    }
                }
                case SendDocument sendDocument -> {
                    try {
                        execute(sendDocument);
                    } catch (TelegramApiException e) {
                        log.error("Error enviando SendDocument: {}", e.getMessage());
                    }
                }
                case null -> {
                    returnResponseEmpty();
                }
                default -> {
                    log.warn("Tipo de respuesta no soportado: {}", response.getClass().getName());
                }
            }
        }

        if (!returnedMessages.isEmpty()) {
            for (Message message : returnedMessages) {
                Long userId = message.getChatId();
                int messageId = message.getMessageId();

                UserSessionContext context = SessionManager.getContext(userId);
                context.getBotSession(bot).setMessageIdToDelete(messageId);
                System.out.println("agregado mensajeid a context");
            }
        }
    }

    public void returnResponseEmpty() {
        try {
            execute(new EmptyResponse());
        } catch (Exception e) {
            log.error("Hubo un error enviando un mensaje vacío: {}", e.getMessage());
        }
    }

    private String getFileUrl(Update update) {
        try {
            PhotoSize largestPhoto = update.getMessage().getPhoto().stream()
                    .max(Comparator.comparing(PhotoSize::getFileSize))
                    .orElse(null);

            if (largestPhoto != null) {
                GetFile getFile = new GetFile();
                getFile.setFileId(largestPhoto.getFileId());
                File file = execute(getFile);

                if (file != null && file.getFilePath() != null) {
                    return "https://api.telegram.org/file/bot" + BOT_TOKEN + "/" + file.getFilePath();
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } catch (TelegramApiException e) {
            log.error("Error al interactuar con la API de Telegram para obtener la imagen: {}", e.getMessage());
            throw new RuntimeException("Error al procesar la imagen.");
        } catch (Throwable e) {
            log.error("Error al procesar la imagen con Gemini: {}", e.getMessage());
            throw new RuntimeException("Error al analizar la imagen.");
        }
    }

    protected <T> List<Object> convertToObject(List<T> list) {
        if (list == null) {
            log.info(">>>>>>>> Lista de respuestas null, revisar código");
            return new ArrayList<>();
        }
        List<Object> objectList = new ArrayList<>();
        for (T item : list) {
            objectList.add(item);
        }
        return objectList;
    }

    @Async
    @EventListener
    public void handleMessageRelayToDeleteMessage(MessageRelayToDeleteMessageEvent event) {

        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setChatId(event.getChatId());
        deleteMessage.setMessageId(event.getMessageId());

        try {
            execute(deleteMessage);
            log.info("exito eliminando mensaje marcado");
        } catch (TelegramApiException e) {
            log.error("error eliminando mensaje marcado para eliminar");
        }
    }
}