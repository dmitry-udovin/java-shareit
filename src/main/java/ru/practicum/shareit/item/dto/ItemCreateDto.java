package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ItemCreateDto {

    @NotBlank(message = "Имя не может быть пустым")
    private String name;
    @NotBlank(message = "описание вещи не может оставаться пустым")
    private String description;
    @NotNull
    private Boolean available;

}
