package dev.yonel.wireguardbot.common.services.database;

import dev.yonel.wireguardbot.common.dtos.PeerDto;
import dev.yonel.wireguardbot.common.dtos.UserDto;

public interface PeerDatabaseService {
    void deletePeer(Long peerId, UserDto userDto);
}
