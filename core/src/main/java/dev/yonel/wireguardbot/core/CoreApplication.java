package dev.yonel.wireguardbot.core;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;

import io.github.cdimascio.dotenv.Dotenv;

@Slf4j
@SpringBootApplication(scanBasePackages = "dev.yonel.wireguardbot")
@EnableFeignClients
@EnableCaching
public class CoreApplication {

    public static void main(String[] args) {
        Path envPath = Paths.get(".env.core");

        try {
            if (Files.exists(envPath)) {
                log.info("Cargando variables desde .env.core");
                Dotenv dotenv = Dotenv.configure()
                        .filename(".env.core")
                        .load();

                dotenv.entries().forEach(entry -> {
                    System.setProperty(entry.getKey(), entry.getValue());
                });
            }
        } catch (Exception e) {
            log.error("Error al cargar las variables de entorno", e);
            log.info("Probando");
        }
        SpringApplication.run(CoreApplication.class, args);
    }
}
