package dev.yonel.wireguardbot.agent;

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
@SpringBootApplication
@EnableFeignClients
@EnableCaching
public class AgentApplication {
    public static void main(String[] args) {
        Path envPath = Paths.get(".env.agent");
        try {
            if(Files.exists(envPath)){
                Dotenv dotenv = Dotenv.configure()
                .filename(".env.agent")
                .load();
                log.info("Cargando variables desde .env.agent");
                dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));
            }
        } catch (Exception e) {
            log.info("Error cargando variables locales");
        }
        SpringApplication.run(AgentApplication.class, args);
    }
}
