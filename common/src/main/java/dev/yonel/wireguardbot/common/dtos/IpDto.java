package dev.yonel.wireguardbot.common.dtos;

import java.util.Arrays;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class IpDto {

    private Long id;
    private Integer[] ip;

    public String getIpString() {
        if (this.ip == null || this.ip.length == 0) {
            // FIXME esto es un error garve -> eviar sms al admin.
            log.error("Error obteniendo desde base de datos la dirección IP");
            return "0.0.0.0"; // -> Retornamos esto para evitar errores en el código.
        }

        StringBuilder sb = new StringBuilder();
        for (int i : this.ip) {
            sb.append(i).append(".");
        }

        // Eliminar el último punto
        sb.setLength(sb.length() - 1);

        return sb.toString();
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
