package dev.yonel.wireguardbot.db.entities;

import java.time.LocalDate;

import jakarta.persistence.Column;
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
    private Integer userId;
    private String name;
    @Column(unique = true)
    private String publicKey;
    @Column(unique = true)
    private String privateKey;
    @Column(unique = true)
    private Integer[] ipAssigned;
    private LocalDate paidUpTo;
    private Boolean active;
    private LocalDate createdAt;
}
