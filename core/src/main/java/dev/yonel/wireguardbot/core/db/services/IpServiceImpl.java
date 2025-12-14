package dev.yonel.wireguardbot.core.db.services;

import java.util.List;

import dev.yonel.wireguardbot.common.dtos.IpDto;
import dev.yonel.wireguardbot.common.enums.IpStatus;
import jakarta.transaction.Transactional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;

import dev.yonel.wireguardbot.common.services.database.IpDatabaseService;
import dev.yonel.wireguardbot.core.db.entities.IpEntity;
import dev.yonel.wireguardbot.core.db.repositories.IpRepository;

@Service
public class IpServiceImpl implements IpDatabaseService {

    private final IpRepository ipRepository;
    private static final List<String> SUBNET_PREFIXES = List.of(
            "10.0.0.",
            "10.0.1."
    );
    private final int MIN_IP_OCTET = 10; // Rango inferior para el último octeto
    private final int MAX_IP_OCTET = 254; // Rango superior para el último octeto

    public IpServiceImpl(IpRepository ipRepository){
        this.ipRepository = ipRepository;
    }

    @Transactional
    @Override
    public IpDto getNewIp() {
        List<IpEntity> existingIps = ipRepository.findAllByOrderByIpAddressAsc();

        for(String subnet : SUBNET_PREFIXES){
            String freeIp = findFreeIpInSubnet(existingIps, subnet);
            IpEntity savedIp;
            if(freeIp != null){
                // Persistimos inmediatamente para reservar la IP
                savedIp = ipRepository.save(
                        IpEntity.builder()
                                .ipAddress(freeIp)
                                .status(IpStatus.RESERVED) // Reservamos la ip
                                .build()
                );
                return convertToDto(savedIp);
            }
        }

        throw new IllegalStateException(
                "No hay direcciones IP disponibles en los rangos configurados"
        );
    }

    @Override
    public void deleteIp(@NotNull IpDto ipDto){
        ipRepository.deleteById(ipDto.getId());
    }

    /**
     * Busca una IP libre dentro de una subred concreta.
     *
     * @param existingIps lista de ip que están en la base de datos.
     * @param subnetPrefix prefijo de red a buscar.
     * @return una ip que este libre o null en caso de que no halle una.
     */
    @Nullable
    private String findFreeIpInSubnet(List<IpEntity> existingIps, String subnetPrefix){
        int expectedOctet = MIN_IP_OCTET;

        for(IpEntity ipEntity : existingIps){
            String ip = ipEntity.getIpAddress();

            if(ipEntity.getStatus() == IpStatus.ASSIGNED || ipEntity.getStatus() == IpStatus.RESERVED){
                continue;
            }

            if(!ip.startsWith(subnetPrefix)){
                continue;
            }

            int lastOctet = Integer.parseInt(ip.substring(subnetPrefix.length()));

            if(lastOctet < MIN_IP_OCTET || lastOctet > MAX_IP_OCTET){
                continue;
            }

            if(lastOctet == expectedOctet){
                expectedOctet ++;
            }else if(lastOctet > expectedOctet){
                return subnetPrefix + expectedOctet;
            }
        }

        if(expectedOctet <= MAX_IP_OCTET){
            return subnetPrefix + expectedOctet;
        }

        return null; // subnet full
    }

    @NotNull
    private IpDto convertToDto(IpEntity entity){
        IpDto ipDto = new IpDto();
        ipDto.setId(entity.getId());
        ipDto.setIpString(entity.getIpAddress());
        ipDto.setStatus(entity.getStatus());
        return ipDto;
    }
}
