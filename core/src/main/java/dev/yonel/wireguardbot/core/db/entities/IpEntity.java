package dev.yonel.wireguardbot.core.db.entities;

import dev.yonel.wireguardbot.common.enums.IpStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity()
@Table(name = "ip_address")
public class IpEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "ip_address", nullable = false, unique = true)
    private String ipAddress;
    private IpStatus status;
}
