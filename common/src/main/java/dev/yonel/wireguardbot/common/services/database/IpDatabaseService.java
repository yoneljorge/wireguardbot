package dev.yonel.wireguardbot.common.services.database;

import dev.yonel.wireguardbot.common.dtos.IpDto;

public interface IpDatabaseService {

    IpDto getNewIp();
    void deleteIp(IpDto ipDto);
}
