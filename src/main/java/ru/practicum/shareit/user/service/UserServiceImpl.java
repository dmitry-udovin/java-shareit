package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.exception.EmailAlreadyUserException;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;

    private static long counter = 1;

    @Override
    public UserResponseDto saveUser(UserCreateDto userCreateDto) {

        User user = UserMapper.userDtoToUser(userCreateDto);
        user.setId(counter);
        counter++;

        checkExistsEmail(user.getEmail());
        User savedUser = userStorage.save(user);

        return UserMapper.userToResponseDto(savedUser);
    }

    @Override
    public UserResponseDto updateUser(Long userId, UserDto userDto) throws UserNotFoundException {

        User existsUser = userStorage.findById(userId);
        if (userDto.getEmail() != null) {
            checkExistsEmail(userDto.getEmail());
        }
        UserMapper.updateUserFromDto(userDto, existsUser);

        return UserMapper.userToResponseDto(existsUser);
    }

    @Override
    public UserResponseDto getUserById(Long userId) throws UserNotFoundException {

        return UserMapper.userToResponseDto(userStorage.findById(userId));
    }

    @Override
    public List<UserResponseDto> getAllUsers() {
        return userStorage.getAllUsers().stream()
                .map(UserMapper::userToResponseDto).toList();
    }

    @Override
    public UserResponseDto deleteUser(Long userId) throws UserNotFoundException {
        return UserMapper.userToResponseDto(userStorage.deleteById(userId));
    }

    private void checkExistsEmail(String email) {
        for (User obj : userStorage.getAllUsers()) {
            if (email.equals(obj.getEmail())) {
                throw new EmailAlreadyUserException("Отказано. Пользователь с данной почтой уже существует");
            }
        }
    }

}
