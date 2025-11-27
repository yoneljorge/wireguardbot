package dev.yonel.wireguardbot.agent;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@EnableCaching
public class AgentApplication {
    public static void main(String[] args) {
        
        Path envPath = Paths.get(".agent.env");

        try {
            if(Files.exists(envPath)){
                //FIXME Quitar SysOut
                System.out.println("Cargando variables desde .env");
                Dotenv dotenv = Dotenv.configure()
                .filename(".agent.env")
                .load();

                dotenv.entries().forEach(entry -> {
                    System.setProperty(entry.getKey(), entry.getValue());
                });
            }else{
                //FIXME Quitar SysOut
                System.out.println("Utilizando las variable de entornos.");
            }
        } catch (Exception e) {
            
        }
        
        SpringApplication.run(AgentApplication.class, args);
    }
}
