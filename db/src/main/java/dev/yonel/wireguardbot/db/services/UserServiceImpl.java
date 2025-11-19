package dev.yonel.wireguardbot.db.services;

import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;

import dev.yonel.wireguardbot.common.dtos.UserDto;
import dev.yonel.wireguardbot.common.exceptions.DuplicateUserException;
import dev.yonel.wireguardbot.common.exceptions.NotExistsException;
import dev.yonel.wireguardbot.common.services.UserService;
import dev.yonel.wireguardbot.db.entities.UserEntity;
import dev.yonel.wireguardbot.db.repositories.UserRepository;

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
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<UserDto> createUser(UserDto user) throws DuplicateUserException, InternalError {
        try {
            UserEntity newEntity = userRepository.save(convertToEntity(user));
            return Optional.ofNullable(convertToDto(newEntity));
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateUserException();
        } catch (Exception e) {
            throw new InternalError(e.getMessage());
        }
    }

    @Override
    public Optional<UserDto> getUser(Long id) throws IllegalArgumentException {
        try {
            Optional<UserEntity> entity = userRepository.findById(id);
            if (!entity.isPresent()) {
                return Optional.empty();
            }
            return Optional.ofNullable(convertToDto(entity.get()));
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("El id no puede ser null");
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public Optional<UserDto> getUserByUserId(Long userId) throws IllegalArgumentException {
        if (userId == null) {
            throw new IllegalArgumentException("UserId is null.");
        }
        try {
            Optional<UserEntity> entity = userRepository.findByUserId(userId);
            if (!entity.isPresent()) {
                return Optional.empty();
            }
            return Optional.ofNullable(convertToDto(entity.get()));
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public Optional<UserDto> updateUser(UserDto user) throws NotExistsException {
        if (user == null) {
            throw new IllegalArgumentException("User is null.");
        }
        try {
            if (userRepository.existsById(user.getId())) {
                UserEntity entity = userRepository.save(convertToEntity(user));
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
        userRepository.delete(convertToEntity(user));
    }

    private UserEntity convertToEntity(UserDto user) {
        return UserEntity.builder()
                .userId(user.getUserId())
                .userName(user.getUserName())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .publicKey(user.getPublicKey())
                .privateKey(user.getPrivateKey())
                .ipAssigned(user.getIpAssigned())
                .paidUpTo(user.getPaidUpTo())
                .active(user.getActive())
                .createdAt(user.getCreatedAt())
                .typeRol(user.getTypeRol())
                .build();
    }

    private UserDto convertToDto(UserEntity entity) {
        return UserDto.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .userName(entity.getUserName())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .publicKey(entity.getPublicKey())
                .privateKey(entity.getPrivateKey())
                .ipAssigned(entity.getIpAssigned())
                .paidUpTo(entity.getPaidUpTo())
                .active(entity.getActive())
                .createdAt(entity.getCreatedAt())
                .typeRol(entity.getTypeRol())
                .build();
    }

    @Override
    public boolean userExistsByUserId(Integer userId) {
        return userRepository.existsByUserId(userId);
    }
}
