package dev.yonel.wireguardbot.common.dtos;

import java.time.LocalDate;
import java.util.List;

import dev.yonel.wireguardbot.common.dtos.telegram.MessageBody;
import dev.yonel.wireguardbot.common.enums.TypeRol;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
	private Long id;
	private Long userId;
	private String userName;
	private String firstName;
	private String lastName;
	private List<PeerDto> peers;
	private TypeRol typeRol;
	private LocalDate activedFreePlan;
    private LocalDate subscriptionPayTo;
	private boolean freePlanEnded;

	public UserDto(MessageBody body) {

		this.setUserId(body.getUserid());

		if (body.getFirstName() != null) {
			this.setFirstName(body.getFirstName());
		} else {
			this.setFirstName("");
		}

		if (body.getLastName() != null) {
			this.setLastName(body.getLastName());
		} else {
			this.setLastName("");
		}

		if (body.getUserName() != null) {
			this.setUserName(body.getUserName());
		} else {
			this.setUserName("");
		}

		this.setTypeRol(TypeRol.USUARIO);
	}

	/**
	 * Agregamos un nuevo peer.
	 * 
	 * @param peer el peer que se desea agregar.
	 */
	public void setPeer(PeerDto peer) {
		this.peers.add(peer);
	}
}
