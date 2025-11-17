package dev.yonel.wireguardbot.db.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.yonel.wireguardbot.db.entities.UserEntity;


@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

}
