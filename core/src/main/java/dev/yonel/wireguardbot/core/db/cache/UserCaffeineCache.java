package dev.yonel.wireguardbot.core.db.cache;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import dev.yonel.wireguardbot.core.db.entities.UserEntity;
import dev.yonel.wireguardbot.core.db.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class UserCaffeineCache extends CaffeineCacheBase {

    private final Cache<Long, UserEntity> cacheById = Caffeine.newBuilder()
            .expireAfterWrite(Duration.ofDays(7))
            .build();

    private final Cache<Long, UserEntity> cacheByUserId = Caffeine.newBuilder()
            .expireAfterWrite(Duration.ofDays(7))
            .build();

    @Autowired
    private UserRepository userRepository;

    public UserEntity findById(Long id){
        return cacheById.get(id, key -> userRepository.findById(id).orElse(null));
    }

    public UserEntity findByUserId(Long userId){
        return cacheByUserId.get(userId, key -> userRepository.findByUserId(userId).orElse(null));
    }

    public UserEntity save(UserEntity entity){
        UserEntity saved = userRepository.saveAndFlush(entity);
        cacheById.put(saved.getId(), saved);
        cacheByUserId.put(saved.getUserId(), saved);

        return saved;
    }

    public void delete(UserEntity entity){
        invalidateCache(entity);
        userRepository.delete(entity);
    }

    public void invalidateCache(UserEntity entity){
        cacheById.invalidate(entity.getId());
        cacheByUserId.invalidate(entity.getUserId());
    }
}
