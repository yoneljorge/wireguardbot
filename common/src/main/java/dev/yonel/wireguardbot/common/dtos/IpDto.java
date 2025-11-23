package dev.yonel.wireguardbot.common.dtos;

import java.util.Arrays;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IpDto {

    private Long id;
    private Integer[] ip;

    public String getIpString() {
        String ip = "";
        for (int i : this.ip) {
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

        this.ip = Arrays.stream(parts)
                .map(Integer::valueOf)
                .toArray(Integer[]::new);
    }
}
