package ru.practicum.shareit.request.dto;

import ru.practicum.shareit.item.dto.ItemShortDto;

import java.time.LocalDateTime;
import java.util.List;

public record ItemRequestDto(long id,
                             String description,
                             long requesterId,
                             LocalDateTime created,
                             List<ItemShortDto> items
) {}