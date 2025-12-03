package dev.yonel.wireguardbot.core.db.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.yonel.wireguardbot.core.db.entities.IpEntity;

public interface IpRepository extends JpaRepository<IpEntity, Long>{
    List<IpEntity> findAllByOrderByIpAddressAsc();
}
