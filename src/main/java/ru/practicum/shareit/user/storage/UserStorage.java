package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserStorage {

    User save(User user);

    User update(User user);

    User findById(Long userId);

    User deleteById(Long userId);

    List<User> getAllUsers();

}
