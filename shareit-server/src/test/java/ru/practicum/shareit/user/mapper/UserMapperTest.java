package ru.practicum.shareit.user.mapper;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.model.User;

import static org.assertj.core.api.Assertions.assertThat;

class UserMapperTest {

    @Test
    void updateUserFromDto_updatesNameWhenNonBlank() {
        User user = new User();
        user.setName("old");
        user.setEmail("e@e.e");
        UserMapper.updateUserFromDto(new UserUpdateDto("new", null), user);
        assertThat(user.getName()).isEqualTo("new");
        assertThat(user.getEmail()).isEqualTo("e@e.e");
    }

    @Test
    void updateUserFromDto_skipsBlankName() {
        User user = new User();
        user.setName("keep");
        user.setEmail("e@e.e");
        UserMapper.updateUserFromDto(new UserUpdateDto("   ", "x@y.z"), user);
        assertThat(user.getName()).isEqualTo("keep");
        assertThat(user.getEmail()).isEqualTo("x@y.z");
    }

    @Test
    void updateUserFromDto_skipsNullName() {
        User user = new User();
        user.setName("keep");
        user.setEmail("a@a.a");
        UserMapper.updateUserFromDto(new UserUpdateDto(null, "b@b.b"), user);
        assertThat(user.getName()).isEqualTo("keep");
        assertThat(user.getEmail()).isEqualTo("b@b.b");
    }

    @Test
    void updateUserFromDto_skipsBlankEmail() {
        User user = new User();
        user.setName("n");
        user.setEmail("old@old.old");
        UserMapper.updateUserFromDto(new UserUpdateDto("n2", "   "), user);
        assertThat(user.getName()).isEqualTo("n2");
        assertThat(user.getEmail()).isEqualTo("old@old.old");
    }

    @Test
    void updateUserFromDto_skipsNullEmail() {
        User user = new User();
        user.setName("n");
        user.setEmail("keep@keep.keep");
        UserMapper.updateUserFromDto(new UserUpdateDto("n3", null), user);
        assertThat(user.getName()).isEqualTo("n3");
        assertThat(user.getEmail()).isEqualTo("keep@keep.keep");
    }

    @Test
    void userDtoToUser_mapsFields() {
        User u = UserMapper.userDtoToUser(new UserCreateDto("nm", "nm@nm.nm", null));
        assertThat(u.getName()).isEqualTo("nm");
        assertThat(u.getEmail()).isEqualTo("nm@nm.nm");
    }

    @Test
    void userToResponseDto_mapsFields() {
        User u = new User();
        u.setId(5L);
        u.setName("a");
        u.setEmail("a@a.a");
        assertThat(UserMapper.userToResponseDto(u).id()).isEqualTo(5);
        assertThat(UserMapper.userToResponseDto(u).name()).isEqualTo("a");
    }
}
