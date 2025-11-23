package dev.yonel.wireguardbot.db.services;

import java.util.List;

import org.springframework.stereotype.Service;

import dev.yonel.wireguardbot.common.services.database.IpService;
import dev.yonel.wireguardbot.db.entities.IpEntity;
import dev.yonel.wireguardbot.db.repositories.IpRepository;

@Service
public class IpServiceImpl implements IpService{

    private final IpRepository ipRepository;
    private final String BASE_IP_SUBNET_PREFIX = "10.0.0."; // Prefijo de la IP
    private final int MIN_IP_OCTET = 2; // Rango inferior para el último octeto
    private final int MAX_IP_OCTET = 254; // Rango superior para el último octeto

    public IpServiceImpl(IpRepository ipRepository){
        this.ipRepository = ipRepository;
    }

    @Override
    public String getNewIp() {
        List<IpEntity> existingIps = ipRepository.findAllByOrderByIpAddressAsc();

        int expectedOctet = MIN_IP_OCTET;
        for (IpEntity ipEntity : existingIps) {
            String currentIp = ipEntity.getIpAddress();
            // Extraer el último octeto de la IP actual
            int lastOctet = Integer.parseInt(currentIp.substring(BASE_IP_SUBNET_PREFIX.length()));

            if (lastOctet == expectedOctet) {
                expectedOctet++;
            } else if (lastOctet > expectedOctet) {
                // Hemos encontrado un hueco
                String newIpAddress = BASE_IP_SUBNET_PREFIX + expectedOctet;
                //IpEntity newIp = IpEntity.builder().ipAddress(newIpAddress).build();
                //FIXME Verificar si es necesario guardar en base de datos
                //ipRepository.save(newIp);
                return newIpAddress;
            }
            // Si lastOctet < expectedOctet, significa que hay IPs duplicadas o mal ordenadas,
            // lo cual no debería pasar con findAllByOrderByIpAddressAsc y una buena gestión de IPs.
            // En un caso real, podrías querer registrar un error o manejarlo de otra forma.
        }

        // Si llegamos aquí, significa que no hay huecos intermedios, asignamos la siguiente IP
        // después de la última IP existente o la primera si no hay IPs.
        if (expectedOctet <= MAX_IP_OCTET) {
            String newIpAddress = BASE_IP_SUBNET_PREFIX + expectedOctet;
            //IpEntity newIp = IpEntity.builder().ipAddress(newIpAddress).build();
            //FIXME Verificar si es necesario guardar en base de datos.
            //ipRepository.save(newIp);
            return newIpAddress;
        } else {
            throw new IllegalStateException("No hay direcciones IP disponibles en el rango " + BASE_IP_SUBNET_PREFIX + MIN_IP_OCTET + " - " + BASE_IP_SUBNET_PREFIX + MAX_IP_OCTET + ".");
        }
    }
}
