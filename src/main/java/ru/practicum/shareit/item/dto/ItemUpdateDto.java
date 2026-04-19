package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ItemUpdateDto {

    private String name;
    @Size(max = 150, message = "Максимум 150 символов")
    private String description;
    private Boolean available;

}
