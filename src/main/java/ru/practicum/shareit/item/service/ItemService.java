package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;

import java.util.List;

public interface ItemService {

    ItemResponseDto saveItem(ItemCreateDto createDto, Long ownerId);

    ItemResponseDto updateItem(ItemUpdateDto itemDto, Long ownerId, Long itemId);

    ItemResponseDto getItemById(Long itemId);

    List<ItemResponseDto> getAllItemsInUserOwn(Long userId);

    List<ItemResponseDto> getItemsBySearch(String text);

}
