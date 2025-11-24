package dev.yonel.wireguardbot.db.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import dev.yonel.wireguardbot.db.entities.UserEntity;


@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    /**
     * Busca un usuario por su ID externo (userId).
     * El tipo de retorno Optional<UserEntity> es ideal para búsquedas que pueden no tener resultado.
     * @param userId El ID del usuario (debe ser Long)
     * @return Un Optional que contiene el UserEntity si se encuentra.
     */
    Optional<UserEntity> findByUserId(Long userId);

    /**
     * Comprueba si existe un usuario con un ID externo específico.
     * Importante: El tipo de parámetro debe coincidir con el tipo de la entidad (Long).
     * @param userId El ID del usuario (Long)
     * @return true si existe un usuario con ese ID, false en caso contrario.
     */
    @Transactional(readOnly = true)
    boolean existsByUserId(Long userId);
}
