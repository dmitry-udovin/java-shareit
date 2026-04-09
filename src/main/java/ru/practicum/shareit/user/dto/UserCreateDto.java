package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserCreateDto {

    @NotBlank(message = "должен быть указан login")
    private String name;
    @NotNull(message = "email не может отсутствовать")
    @NotBlank(message = "email не может быть пустым")
    @Email
    private String email;
    private String password;

}
