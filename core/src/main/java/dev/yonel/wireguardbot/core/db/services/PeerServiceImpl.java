package dev.yonel.wireguardbot.core.db.services;

import dev.yonel.wireguardbot.common.dtos.PeerDto;
import dev.yonel.wireguardbot.common.dtos.UserDto;
import dev.yonel.wireguardbot.common.services.database.PeerDatabaseService;
import dev.yonel.wireguardbot.common.services.database.UserDatabaseService;
import dev.yonel.wireguardbot.core.db.repositories.PeerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PeerServiceImpl implements PeerDatabaseService {

    @Autowired
    private UserDatabaseService userDatabaseService;

    @Autowired
    private PeerRepository peerRepository;

    @Override
    public void deletePeer(Long peerId, UserDto userDto) {
        peerRepository.deleteById(peerId);
        userDatabaseService.invalidateCache(userDto);
    }
}
