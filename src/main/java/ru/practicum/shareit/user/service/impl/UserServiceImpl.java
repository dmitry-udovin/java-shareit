package ru.practicum.shareit.user.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
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
public class UserServiceImpl implements UserService {

    private long counter = 1;

    private final UserRepository repository;

    @Override
    public UserResponseDto saveUser(UserCreateDto userCreateDto) {

        User user = UserMapper.userDtoToUser(userCreateDto);
        user.setId(counter);
        counter++;

        checkExistsEmail(user.getEmail());
        User savedUser = repository.save(user);

        return UserMapper.userToResponseDto(savedUser);
    }

    @Override
    public UserResponseDto updateUser(Long userId, UserUpdateDto userDto) {

        User existsUser = repository.findById(userId).orElseThrow(() -> new UserNotFoundException("Пользователь с указанным номером отсутствует."));
        if (userDto.getEmail() != null) {
            checkExistsEmail(userDto.getEmail());
        }
        UserMapper.updateUserFromDto(userDto, existsUser);

        return UserMapper.userToResponseDto(existsUser);
    }

    @Override
    public UserResponseDto getUserById(Long userId) {

        return UserMapper.userToResponseDto(repository.findById(userId).orElseThrow(() ->
                new UserNotFoundException("Пользователь с указанным номером отсутствует.")));
    }

    @Override
    public List<UserResponseDto> getAllUsers() {
        return repository.findAll().stream()
                .map(UserMapper::userToResponseDto).toList();
    }

    @Override
    public UserResponseDto deleteUser(Long userId) {
        return UserMapper.userToResponseDto(repository.deleteUserById(userId));
    }

    private void checkExistsEmail(String email) {
        for (User obj : repository.findAll()) {
            if (email.equals(obj.getEmail())) {
                throw new EmailAlreadyUserException("Отказано. Пользователь с данной почтой уже существует");
            }
        }
    }

}
