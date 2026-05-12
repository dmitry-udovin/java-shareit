package ru.practicum.shareit.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "ru.practicum.shareit.gateway")
public class ShareItGatewayApp {

    public static void main(String[] args) {
        SpringApplication.run(ShareItGatewayApp.class, args);
    }
}
