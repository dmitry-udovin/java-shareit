package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.Size;

public record ItemUpdateDto(String name, @Size(max = 150, message = "Максимум 150 символов") String description,
                            Boolean available) {

}
