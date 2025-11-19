package dev.yonel.wireguardbot.db.entities;

import java.time.LocalDate;

import dev.yonel.wireguardbot.common.enums.TypeRol;
import dev.yonel.wireguardbot.db.crypto.SensitiveDataConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Entity(name = "usuarios")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private Long userId;

    @Convert(converter = SensitiveDataConverter.class)
    private String userName;

    @Convert(converter = SensitiveDataConverter.class)
    private String firstName;

    @Convert(converter = SensitiveDataConverter.class)
    private String lastName;

    @Convert(converter = SensitiveDataConverter.class)
    @Column(unique = true)
    private String publicKey;

    @Convert(converter = SensitiveDataConverter.class)
    @Column(unique = true)
    private String privateKey;

    @Column(unique = true)
    private Integer[] ipAssigned;

    private LocalDate paidUpTo;
    private Boolean active;
    private LocalDate createdAt;
    private TypeRol typeRol;
}
