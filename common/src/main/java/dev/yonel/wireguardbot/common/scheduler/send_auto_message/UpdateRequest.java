
package dev.yonel.wireguardbot.common.scheduler.send_auto_message;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

public class UpdateRequest {

	@JsonProperty("update_id")
	private long updateId;
	private Message message;

	// Getters y Setters

	public long getUpdateId() {
		return updateId;
	}

	public void setUpdateId(long updateId) {
		this.updateId = updateId;
	}

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

	// Clase interna para el mensaje (puedes expandir según sea necesario)
	@Getter
	@Setter
	public static class Message {
		private long messageId;
		private From from;
		private Chat chat;
		private long date;
		private String text;

		// Getters y Setters

		// Clase interna para "from"
		@Getter
		@Setter
		public static class From {
			private long id;
			private boolean isBot;
			private String firstName;
			private String username;

			// Getters y Setters
		}

		// Clase interna para "chat"
		@Getter
		@Setter
		public static class Chat {
			private long id;
			private String firstName;
			private String username;
			private String type;

			// Getters y Setters
		}
	}
}
