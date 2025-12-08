package dev.yonel.wireguardbot.common.dtos;

import java.time.LocalDate;

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
public class PeerDto {
    private Long id;
    private String privateKey;
    private String publicKey;
    private IpDto ipDto;
    private LocalDate createdAt;
    private LocalDate paidUpTo;
    private boolean active;
}
