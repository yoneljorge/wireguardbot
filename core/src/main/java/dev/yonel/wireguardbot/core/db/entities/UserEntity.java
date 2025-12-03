package dev.yonel.wireguardbot.core.db.entities;

import java.time.LocalDate;
import java.util.List;

import dev.yonel.wireguardbot.common.enums.TypeRol;
import dev.yonel.wireguardbot.core.db.crypto.SensitiveDataConverter;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private Long userId;

    @Convert(converter = SensitiveDataConverter.class)
    private String userName;

    @Convert(converter = SensitiveDataConverter.class)
    private String firstName;

    @Convert(converter = SensitiveDataConverter.class)
    private String lastName;

    @OneToMany(
        mappedBy = "user",
        cascade = CascadeType.ALL,
        orphanRemoval = true,
        fetch = FetchType.LAZY
    )
    private List<PeerEntity> peers;
    
    private TypeRol typeRol;
    private LocalDate activedFreePlan;
    private Boolean freePlanEnded;
}
