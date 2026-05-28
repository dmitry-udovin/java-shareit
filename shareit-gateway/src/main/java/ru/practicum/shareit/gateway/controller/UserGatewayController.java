package ru.practicum.shareit.gateway.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.gateway.client.BaseClient;
import ru.practicum.shareit.gateway.client.UserProxyClient;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Validated
public class UserGatewayController {

    private final UserProxyClient userProxyClient;

    @PostMapping
    public ResponseEntity<String> create(@Valid @RequestBody UserCreateDto dto) {
        return BaseClient.forward(userProxyClient.create(dto));
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<String> update(@PathVariable long userId, @Valid @RequestBody UserUpdateDto dto) {
        return BaseClient.forward(userProxyClient.update(userId, dto));
    }

    @GetMapping
    public ResponseEntity<String> findAll() {
        return BaseClient.forward(userProxyClient.findAll());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<String> findById(@PathVariable long userId) {
        return BaseClient.forward(userProxyClient.findById(userId));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> delete(@PathVariable long userId) {
        return BaseClient.forward(userProxyClient.delete(userId));
    }
}
