package ru.practicum.shareit.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.nio.file.Files;
import java.nio.file.Path;

@SpringBootApplication(scanBasePackages = "ru.practicum.shareit.gateway")
public class ShareItGatewayApp {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(ShareItGatewayApp.class);
        if (Files.exists(Path.of("/.dockerenv"))) {
            app.setAdditionalProfiles("docker");
        }
        app.run(args);
    }
}
