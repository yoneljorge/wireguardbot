package dev.yonel.wireguardbot.db.services;

import org.springframework.dao.DataIntegrityViolationException;

import dev.yonel.wireguardbot.common.dtos.UserDto;
import dev.yonel.wireguardbot.common.exceptions.DuplicateUserException;
import dev.yonel.wireguardbot.common.services.UserService;
import dev.yonel.wireguardbot.db.entities.UserEntity;
import dev.yonel.wireguardbot.db.repositories.UserRepository;

/**
 * Implementación interna de UserService.
 * Esta clase no debe ser accedida directamente desde otros módulos.
 * Use la interfaz UserService del módulo common.
 * 
 * @apiNote Esta clase es pública solo para permitir la auto-configuración de Spring.
 *          No debe ser instanciada o referenciada directamente desde fuera del módulo db.
 */
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    @Override
    public UserDto createUser(UserDto user) throws DuplicateUserException, InternalError{
        try {
            UserEntity newEntity = userRepository.save(convertToEntity(user));
            return convertToDto(newEntity);
        }catch (DataIntegrityViolationException e){    
            throw new DuplicateUserException();
        } catch (Exception e) {
            throw new InternalError(e.getMessage());
        }
    }

    @Override
    public UserDto getUser() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getUser'");
    }

    @Override
    public UserDto updateUser() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateUser'");
    }

    @Override
    public boolean deleteUser() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteUser'");
    }

    private UserEntity convertToEntity(UserDto user){
        return UserEntity.builder()
        .userId(user.getUserId())
        .name(user.getName())
        .publicKey(user.getPublicKey())
        .privateKey(user.getPrivateKey())
        .ipAssigned(user.getIpAssigned())
        .paidUpTo(user.getPaidUpTo())
        .active(user.getActive())
        .createdAt(user.getCreatedAt())
        .build();
    }

    private UserDto convertToDto(UserEntity entity){
        return UserDto.builder()
        .id(entity.getId())
        .userId(entity.getUserId())
        .name(entity.getName())
        .publicKey(entity.getPublicKey())
        .privateKey(entity.getPrivateKey())
        .ipAssigned(entity.getIpAssigned())
        .paidUpTo(entity.getPaidUpTo())
        .active(entity.getActive())
        .createdAt(entity.getCreatedAt())
        .build();
    }

}
