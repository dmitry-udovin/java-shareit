package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;

public record ItemCreateDto(@NotBlank(message = "Имя не может быть пустым") String name,
                            @NotBlank(message = "описание вещи не может оставаться пустым") String description,
                            boolean available,
                            Long requestId) {

}
