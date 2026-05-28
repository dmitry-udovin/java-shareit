package ru.practicum.shareit.request.mapper;

import ru.practicum.shareit.item.dto.ItemShortDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

public class ItemRequestMapper {

    public static ItemRequestResponseDto toResponseDto(ItemRequest request) {
        return new ItemRequestResponseDto(
                request.getId(),
                request.getDescription(),
                request.getRequester().getId(),
                request.getCreated(),
                List.of()
        );
    }

    public static ItemRequestResponseDto toResponseDtoWithItems(ItemRequest request, List<Item> items) {
        List<ItemShortDto> itemDtos = items.stream()
                .map(item -> new ItemShortDto(
                        item.getId(),
                        item.getName(),
                        item.getOwnerId()
                ))
                .toList();

        return new ItemRequestResponseDto(
                request.getId(),
                request.getDescription(),
                request.getRequester().getId(),
                request.getCreated(),
                itemDtos
        );
    }
}