package dev.yonel.wireguardbot.core;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication(scanBasePackages = "dev.yonel.wireguardbot")
@EnableCaching
@EnableFeignClients
@EnableDiscoveryClient
public class CoreApplication {

    private static final Logger logger = LoggerFactory.getLogger(CoreApplication.class);

    public static void main(String[] args) {
        Path envPath = Paths.get(".core.env");

        try {
            if (Files.exists(envPath)) {
                logger.info("Cargando variables desde .core.env");
                Dotenv dotenv = Dotenv.configure()
                        .filename(".core.env")
                        .load();

                dotenv.entries().forEach(entry -> {
                    System.setProperty(entry.getKey(), entry.getValue());
                });
            }
        } catch (Exception e) {
            logger.error("Error al cargar las variables de entorno", e);
            logger.info("Probando");
        }
        SpringApplication.run(CoreApplication.class, args);
    }
}
