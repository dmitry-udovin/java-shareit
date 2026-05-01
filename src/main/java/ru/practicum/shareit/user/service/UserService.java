package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.dto.UserResponseDto;

import java.util.List;

public interface UserService {

    UserResponseDto saveUser(UserCreateDto userCreateDto);

    UserResponseDto updateUser(Long userId, UserUpdateDto userDto);

    UserResponseDto getUserById(Long userId);

    List<UserResponseDto> getAllUsers();

    UserResponseDto deleteUser(Long userId);

}
