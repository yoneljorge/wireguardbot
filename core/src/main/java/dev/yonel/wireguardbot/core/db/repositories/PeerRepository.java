package dev.yonel.wireguardbot.core.db.repositories;

import dev.yonel.wireguardbot.core.db.entities.PeerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PeerRepository extends JpaRepository<PeerEntity, Long> {
}
