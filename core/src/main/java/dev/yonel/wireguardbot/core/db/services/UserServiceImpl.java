package dev.yonel.wireguardbot.core.db.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import dev.yonel.wireguardbot.common.dtos.IpDto;
import dev.yonel.wireguardbot.common.dtos.PeerDto;
import dev.yonel.wireguardbot.common.dtos.UserDto;
import dev.yonel.wireguardbot.common.exceptions.DuplicateUserException;
import dev.yonel.wireguardbot.common.exceptions.NotExistsException;
import dev.yonel.wireguardbot.common.services.database.UserService;
import dev.yonel.wireguardbot.core.db.cache.UserCaffeineCache;
import dev.yonel.wireguardbot.core.db.entities.IpEntity;
import dev.yonel.wireguardbot.core.db.entities.PeerEntity;
import dev.yonel.wireguardbot.core.db.entities.UserEntity;

/**
 * Implementación interna de UserService.
 * Esta clase no debe ser accedida directamente desde otros módulos.
 * Use la interfaz UserService del módulo common.
 * 
 * @apiNote Esta clase es pública solo para permitir la auto-configuración de
 *          Spring.
 *          No debe ser instanciada o referenciada directamente desde fuera del
 *          módulo db.
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserCaffeineCache userCaffeineCache;

    public UserServiceImpl(UserCaffeineCache userCaffeineCache) {
        this.userCaffeineCache = userCaffeineCache;
    }

    @Override
    public Optional<UserDto> createUser(UserDto user) throws DuplicateUserException, InternalError {
        try {
            UserEntity newEntity = userCaffeineCache.save(convertToEntity(user));
            return Optional.ofNullable(convertToDto(newEntity));
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateUserException();
        } catch (Exception e) {
            throw new InternalError(e.getMessage());
        }
    }

    @Override
    public Optional<UserDto> getUser(Long id) throws IllegalArgumentException {
        UserEntity entity = userCaffeineCache.findById(id);
        if (entity == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(convertToDto(entity));

    }

    @Override
    public Optional<UserDto> getUserByUserId(Long userId) throws IllegalArgumentException {
        if (userId == null) {
            throw new IllegalArgumentException("UserId is null.");
        }

        UserEntity entity = userCaffeineCache.findByUserId(userId);
        if (entity == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(convertToDto(entity));

    }

    @Override
    public Optional<UserDto> updateUser(UserDto user) throws NotExistsException {
        if (user == null) {
            throw new IllegalArgumentException("User is null.");
        }
        try {
            if (userCaffeineCache.findById(user.getId()) != null) {
                UserEntity entity = userCaffeineCache.save(convertToEntity(user));
                return Optional.ofNullable(convertToDto(entity));
            } else {
                throw new NotExistsException("No existe el usuario que se desea actualizar.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public void deleteUser(UserDto user) {
        userCaffeineCache.delete(convertToEntity(user));
    }

    private UserEntity convertToEntity(UserDto user) {
        return UserEntity.builder()
                .id(user.getId())
                .userId(user.getUserId())
                .userName(user.getUserName())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .typeRol(user.getTypeRol())
                .activedFreePlan(user.getActivedFreePlan())
                .freePlanEnded(user.isFreePlanEnded())
                .peers(convertPeersToEntitis(user))
                .build();
    }

    private UserDto convertToDto(UserEntity user) {
        return UserDto.builder()
                .id(user.getId())
                .userId(user.getUserId())
                .userName(user.getUserName())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .typeRol(user.getTypeRol())
                .activedFreePlan(user.getActivedFreePlan())
                .freePlanEnded(user.getFreePlanEnded())
                .peers(convertPeersToDto(user))
                .build();
    }

    private List<PeerEntity> convertPeersToEntitis(UserDto user) {
        List<PeerEntity> peers = new ArrayList<>();
        if (user.getPeers() == null) {
            return peers;
        }

        for (PeerDto p : user.getPeers()) {

            PeerEntity peer = PeerEntity.builder()
                    .id(p.getId())
                    .privateKey(p.getPrivateKey())
                    .publicKey(p.getPublicKey())
                    .createdAt(p.getCreatedAt())
                    .paidUpTo(p.getPaidUpTo())
                    .active(p.isActive())
                    .build();

            IpEntity ip = new IpEntity();
            ip.setId(p.getIp().getId());
            ip.setIpAddress(p.getIp().getIpString());

            peer.setIp(ip);

            peers.add(peer);
        }
        return peers;
    }

    private List<PeerDto> convertPeersToDto(UserEntity user) {
        List<PeerDto> peers = new ArrayList<>();

        if (user.getPeers() == null) {
            return peers;
        }

        for (PeerEntity p : user.getPeers()) {
            PeerDto dto = PeerDto.builder()
                    .id(p.getId())
                    .privateKey(p.getPrivateKey())
                    .publicKey(p.getPublicKey())
                    .createdAt(p.getCreatedAt())
                    .paidUpTo(p.getPaidUpTo())
                    .active(p.getActive())
                    .build();

            IpDto ip = new IpDto();
            ip.setId(p.getIp().getId());
            ip.setIpString(p.getIp().getIpAddress());

            dto.setIp(ip);

            peers.add(dto);
        }
        return peers;
    }

    @Override
    public boolean userExistsByUserId(Long userId) {
        return userCaffeineCache.findByUserId(userId) != null;
    }
}
