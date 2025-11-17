package dev.yonel.wireguardbot.common.dtos;

import java.time.LocalDate;
import java.util.Arrays;

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
    private Integer userId;
    private String name;
    private String publicKey;
    private String privateKey;
    private Integer[] ipAssigned;
    private LocalDate paidUpTo;
    private Boolean active;
    private LocalDate createdAt;

    public String getIpString() {
        String ip = "";
        for (int i : this.ipAssigned) {
            ip = ip + i;
        }
        return ip;
    }

    public void setIpString(String ipString) {

        if (ipString == null || ipString.isEmpty()) {
            throw new IllegalArgumentException("La cadena IP no puede ser nula o vacía.");
        }

        String[] parts = ipString.split("\\.");

        if (parts.length != 4) {
            throw new IllegalArgumentException("El formato de la IP debe tener 4 segmentos (ej: X.X.X.X).");
        }

        this.ipAssigned = Arrays.stream(parts)
                .map(Integer::valueOf)
                .toArray(Integer[]::new);
    }
}
