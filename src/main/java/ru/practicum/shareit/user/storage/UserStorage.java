package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserStorage {

    User save(User user);

    User update(User user) throws UserNotFoundException;

    User findById(Long userId) throws UserNotFoundException;

    User deleteById(Long userId) throws UserNotFoundException;

    List<User> getAllUsers();

}
