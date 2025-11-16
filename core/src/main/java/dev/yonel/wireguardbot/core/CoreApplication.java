package dev.yonel.wireguardbot.core;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.cloud.openfeign.EnableFeignClients;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
@EnableEurekaServer
@EnableFeignClients
public class CoreApplication {

    public static void main (String[] args){
        Path envPath = Paths.get(".core.env");

        try {
            if(Files.exists(envPath)){
                //FIXME Quitar SysOut
                System.out.println("Cargando variables desde .env");
                Dotenv dotenv = Dotenv.configure()
                .filename(".core.env")
                .load();

                dotenv.entries().forEach(entry -> {
                    System.setProperty(entry.getKey(), entry.getValue());
                });
            }else{
                //FIXME Quitar SysOut
                System.out.println("Utilizando las variable de entornos.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        SpringApplication.run(CoreApplication.class, args);
    }
}
