package dev.yonel.wireguardbot.db.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.yonel.wireguardbot.db.entities.UserEntity;


@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByUserId(Long userId);

    boolean existsByUserId(Integer userId);
}
