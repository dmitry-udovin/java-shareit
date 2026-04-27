package ru.practicum.shareit.user.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.exception.EmailAlreadyUserException;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserResponseDto saveUser(UserCreateDto userCreateDto) {

        User user = UserMapper.userDtoToUser(userCreateDto);

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new EmailAlreadyUserException("Email уже занят");
        }
        User savedUser = userRepository.save(user);

        return UserMapper.userToResponseDto(savedUser);
    }

    @Override
    public UserResponseDto updateUser(Long userId, UserUpdateDto userDto) {

        User existsUser = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("Пользователь с указанным номером отсутствует."));
        if (userDto.getEmail() != null) {
            if (userRepository.existsByEmail(userDto.getEmail())) {
                throw new EmailAlreadyUserException("Email уже занят");
            }
        }
        UserMapper.updateUserFromDto(userDto, existsUser);

        return UserMapper.userToResponseDto(existsUser);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDto getUserById(Long userId) {

        return UserMapper.userToResponseDto(userRepository.findById(userId).orElseThrow(() ->
                new UserNotFoundException("Пользователь с указанным номером отсутствует.")));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserMapper::userToResponseDto).toList();
    }

    @Override
    public UserResponseDto deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Пользователя с указанным номером не существует."));
        userRepository.deleteById(userId);
        return UserMapper.userToResponseDto(user);
    }

}
