
package dev.yonel.wireguardbot.message_manager.service.users;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import dev.yonel.wireguardbot.common.context.SessionManager;
import dev.yonel.wireguardbot.common.context.UserSessionContext;
import dev.yonel.wireguardbot.common.dtos.UserDto;
import dev.yonel.wireguardbot.common.dtos.telegram.MessageBody;
import dev.yonel.wireguardbot.common.dtos.telegram.ResponseBody;
import dev.yonel.wireguardbot.common.enums.TypeParseMode;
import dev.yonel.wireguardbot.common.events.RegisterNewUsersFromGroupEvent;
import dev.yonel.wireguardbot.common.events.SendToStartCommandEvent;
import dev.yonel.wireguardbot.common.services.database.UserService;
import dev.yonel.wireguardbot.message_manager.messages.BienvenidaMessage;
import dev.yonel.wireguardbot.common.events.MessageRelayToTelegramBotClientEvent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class RegisterUsers {

	@Autowired
	private UserService userService;

	@Autowired
	private ApplicationEventPublisher publisher;

	private List<ResponseBody> responses;

	@EventListener
	public void handleRegisterNewUsersFromGroup(RegisterNewUsersFromGroupEvent event) {
		List<MessageBody> newUsers = event.getNewUsers();

		for (MessageBody messageBody : newUsers) {

			@SuppressWarnings("unused")
			UserDto user;
			try {
				Optional<UserDto> userOptional = userService.getUserByUserId(messageBody.getUserid());
				if (userOptional.isPresent()) {
					user = userOptional.get();
					try {
						if (!messageBody.getAlias().isBlank()) {
							try {
								userService.createUser(new UserDto(messageBody));
							} catch (Throwable e) {
								log.error("Error intentando guardar el user.");
							}

							sendWelcomeMessageToUser(messageBody);
						}
					} catch (Exception e) {
						/*
						 * En caso de que el user no tenga username o sea nulo se pasa para
						 * startCommand para que entre en contexto y espere a que el user le devuelva
						 * el usernmae;
						 */

						UserSessionContext context = SessionManager.getContext(messageBody.getUserid());
						publisher.publishEvent(new SendToStartCommandEvent(this, messageBody, context));

					}
				}
			} catch (Throwable e) {
				log.error("Error buscando user en base de datos.");
			}
		}

	}

	public void sendWelcomeMessageToUser(MessageBody messageBody) {
		this.responses = new ArrayList<>();

		ResponseBody responseBody = new ResponseBody();
		responseBody.setChatid(messageBody.getChatid());
		responseBody.setUserid(messageBody.getUserid());
		responseBody.setResponse(BienvenidaMessage.message(messageBody.getAlias()));
		responseBody.setParseMode(TypeParseMode.HTML);

		this.responses.add(responseBody);

		publisher.publishEvent(new MessageRelayToTelegramBotClientEvent(this, responses));
	}
}
