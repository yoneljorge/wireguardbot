package dev.yonel.wireguardbot.core.db.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
    @Column(name = "active_free_plan")
    private LocalDate activedFreePlan;
    @Column(name = "subscription_pay_to")
    private LocalDate subscriptionPayTo;
    @Column(name = "free_plan_ended")
    private Boolean freePlanEnded;

    @Builder.Default
    private Boolean active = true;
    @Column(name = "delete_at")
    private LocalDateTime deletedAt;
    @Builder.Default
    private Boolean deleted = false;

    public void deactivate(){
        this.active = false;
        this.deleted = true;
        this.deletedAt = LocalDateTime.now();
    }

    public void reactivate(){
        this.active = true;
        this.deleted = false;
        this.deletedAt = null;
    }

    public boolean isActive(){
        return Boolean.TRUE.equals(active) && !Boolean.TRUE.equals(deleted);
    }
}
