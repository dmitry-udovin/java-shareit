package ru.practicum.shareit.user.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findById(long userId);

    default User deleteUserById(long userId) {
        return findById(userId)
                .map(user -> {
                    deleteById(userId);
                    return user;
                })
                .orElseThrow(() -> new UserNotFoundException("Пользователя с указанным номером не существует."));
    }

}
