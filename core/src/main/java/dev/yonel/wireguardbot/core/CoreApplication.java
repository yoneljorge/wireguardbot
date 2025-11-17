package dev.yonel.wireguardbot.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.cloud.openfeign.EnableFeignClients;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication(scanBasePackages = "dev.yonel.wireguardbot")
@EnableEurekaServer
@EnableFeignClients
public class CoreApplication {

    private static final Logger logger = LoggerFactory.getLogger(CoreApplication.class);

    public static void main(String[] args) {
        Path envPath = Paths.get(".core.env");

        try {
            if (Files.exists(envPath)) {
                logger.info("Cargando variables desde .env");
                Dotenv dotenv = Dotenv.configure()
                        .filename(".core.env")
                        .load();

                dotenv.entries().forEach(entry -> {
                    System.setProperty(entry.getKey(), entry.getValue());
                });
            } else {
                logger.info("Utilizando las variable de entornos.");
            }
        } catch (Exception e) {
            logger.error("Error al cargar las variables de entorno", e);
            logger.info("Probando");
        }
        SpringApplication.run(CoreApplication.class, args);
    }
}
