package dev.yonel.wireguardbot.db.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import dev.yonel.wireguardbot.common.services.database.IpService;
import dev.yonel.wireguardbot.common.services.database.UserService;
import dev.yonel.wireguardbot.db.cache.UserCaffeineCache;
import dev.yonel.wireguardbot.db.repositories.IpRepository;
import dev.yonel.wireguardbot.db.services.IpServiceImpl;
import dev.yonel.wireguardbot.db.services.UserServiceImpl;

/**
 * Auto-configuración del módulo db.
 * Expone los beans necesarios (UserService) sin exponer las implementaciones internas.
 * Configura JPA repositories y entity scanning automáticamente.
 */
@AutoConfiguration
@EnableJpaRepositories(basePackages = "dev.yonel.wireguardbot.db.repositories")
@EntityScan(basePackages = "dev.yonel.wireguardbot.db.entities")
public class DbAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public UserService userService(UserCaffeineCache userCaffeineCache) {
        return new UserServiceImpl(userCaffeineCache);
    }

    @Bean
    @ConditionalOnMissingBean
    public IpService ipService(IpRepository ipRepository){
        return new IpServiceImpl(ipRepository);
    }
}

