package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.exception.EmailAlreadyUserException;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.impl.UserServiceImpl;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class UserServiceImplIntegrationTest {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    void saveUser_persistsAndReturnsDto() {
        UserResponseDto dto = userService.saveUser(new UserCreateDto("Ann", "ann@mail.test", null));

        assertThat(dto.id()).isNotNull();
        assertThat(dto.name()).isEqualTo("Ann");
        assertThat(dto.email()).isEqualTo("ann@mail.test");
        assertThat(userRepository.findById(dto.id())).isPresent();
    }

    @Test
    void saveUser_duplicateEmail_throws() {
        userService.saveUser(new UserCreateDto("A", "dup@mail.test", null));

        assertThatThrownBy(() -> userService.saveUser(new UserCreateDto("B", "dup@mail.test", null)))
                .isInstanceOf(EmailAlreadyUserException.class);
    }

    @Test
    void updateUser_changesFields() {
        UserResponseDto created = userService.saveUser(new UserCreateDto("Old", "old@mail.test", null));

        UserResponseDto updated = userService.updateUser(created.id(), new UserUpdateDto("New", null));

        assertThat(updated.name()).isEqualTo("New");
        assertThat(updated.email()).isEqualTo("old@mail.test");
    }

    @Test
    void getUserById_returnsStored() {
        UserResponseDto created = userService.saveUser(new UserCreateDto("X", "x@mail.test", null));

        UserResponseDto found = userService.getUserById(created.id());

        assertThat(found).isEqualTo(created);
    }

    @Test
    void getUserById_missing_throws() {
        assertThatThrownBy(() -> userService.getUserById(999L))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void getAllUsers_returnsAllRows() {
        userService.saveUser(new UserCreateDto("U1", "u1@mail.test", null));
        userService.saveUser(new UserCreateDto("U2", "u2@mail.test", null));

        List<UserResponseDto> all = userService.getAllUsers();

        assertThat(all).hasSizeGreaterThanOrEqualTo(2);
    }

    @Test
    void deleteUser_removesFromDatabase() {
        UserResponseDto created = userService.saveUser(new UserCreateDto("Del", "del@mail.test", null));

        userService.deleteUser(created.id());

        assertThat(userRepository.findById(created.id())).isEmpty();
    }

    @Test
    void deleteUser_unknownId_throws() {
        assertThatThrownBy(() -> userService.deleteUser(999L))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void updateUser_emailConflict_throws() {
        userService.saveUser(new UserCreateDto("A", "a@mail.test", null));
        UserResponseDto b = userService.saveUser(new UserCreateDto("B", "b@mail.test", null));

        assertThatThrownBy(() -> userService.updateUser(b.id(), new UserUpdateDto(null, "a@mail.test")))
                .isInstanceOf(EmailAlreadyUserException.class);
    }

    @Test
    void getUserById_readsFromDatabase() {
        User u = new User();
        u.setName("Direct");
        u.setEmail("direct@mail.test");
        u = userRepository.save(u);

        UserResponseDto dto = userService.getUserById(u.getId());

        assertThat(dto.name()).isEqualTo("Direct");
        assertThat(dto.email()).isEqualTo("direct@mail.test");
    }
}
