package ru.practicum.shareit.user.mapper;

import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.model.User;

public class UserMapper {

    private UserMapper() {

    }

    public static void updateUserFromDto(UserUpdateDto dto, User user) {
        if (dto.name() != null && !dto.name().isBlank()) {
            user.setName(dto.name());
        }
        if (dto.email() != null && !dto.email().isBlank()) {
            user.setEmail(dto.email());
        }

    }

    public static User userDtoToUser(UserCreateDto userCreateDto) {
        User user = new User();

        user.setName(userCreateDto.name());
        user.setEmail(userCreateDto.email());

        return user;
    }

    public static UserResponseDto userToResponseDto(User user) {

        return new UserResponseDto(user.getId(), user.getName(), user.getEmail());
    }

}
