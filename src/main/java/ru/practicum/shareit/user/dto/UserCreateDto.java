package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserCreateDto(@NotBlank(message = "должен быть указан login") String name,
                            @NotNull(message = "email не может отсутствовать") @NotBlank(message = "email не может быть пустым") @Email String email,
                            String password) {

}
