package dev.yonel.wireguardbot.db.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

import dev.yonel.wireguardbot.db.entities.IpEntity;

public interface IpRepository extends JpaRepository<IpEntity, Long>{
    List<IpEntity> findAllByOrderByIpAddressAsc();
}
