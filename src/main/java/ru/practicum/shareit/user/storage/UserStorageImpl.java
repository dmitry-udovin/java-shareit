package ru.practicum.shareit.user.storage;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class UserStorageImpl implements UserStorage {

    private Map<Long, User> users = new HashMap<>();

    @Override
    public User save(User user) {

        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User userForUpdate) throws UserNotFoundException {
        if (users.containsKey(userForUpdate.getId())) {
            users.put(userForUpdate.getId(), userForUpdate);
            return userForUpdate;
        } else {
            throw new UserNotFoundException("Отсутствует пользователь с указанным id для обновления.");
        }
    }

    @Override
    public User findById(Long userId) throws UserNotFoundException {
        if (users.containsKey(userId)) {
            return users.get(userId);
        } else {
            throw new UserNotFoundException("Пользователь с указанным номером отсутствует.");
        }
    }

    @Override
    public User deleteById(Long userId) throws UserNotFoundException {
        if (users.containsKey(userId)) {
            return users.remove(userId);
        } else {
            throw new UserNotFoundException("Пользователя с указанным номером не существует.");
        }
    }

    @Override
    public List<User> getAllUsers() {
        return users.values().stream().toList();
    }
}
