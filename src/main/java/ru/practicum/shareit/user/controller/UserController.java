package ru.practicum.shareit.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public UserResponseDto createUser(@Valid @RequestBody UserCreateDto createUserDto) {
        log.info("Получен запрос на создание пользователя: {}", createUserDto);
        return userService.saveUser(createUserDto);
    }

    @PatchMapping("/{userId}")
    public UserResponseDto updateUser(@Valid @PathVariable long userId,
                                      @RequestBody UserDto userDto) throws UserNotFoundException {
        log.info("Получен запрос на обновление данных пользователя: {}", userDto);
        return userService.updateUser(userId, userDto);
    }

    @GetMapping
    public List<UserResponseDto> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{userId}")
    public UserResponseDto findUserById(@PathVariable long userId) throws UserNotFoundException {
        log.info("Получен запрос на поиск пользователя по id: {}", userId);
        return userService.getUserById(userId);
    }

    @DeleteMapping("/{userId}")
    public UserResponseDto deleteUserById(@PathVariable long userId) throws UserNotFoundException {
        log.info("Получен запрос на удаление пользователя, id: {}", userId);
        return userService.deleteUser(userId);
    }


}
