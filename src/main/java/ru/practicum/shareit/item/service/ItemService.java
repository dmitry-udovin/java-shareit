package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.user.exception.NotOwnerException;
import ru.practicum.shareit.user.exception.UserNotFoundException;

import java.util.List;

public interface ItemService {

    ItemResponseDto saveItem(ItemCreateDto createDto, Long ownerId) throws UserNotFoundException;

    ItemResponseDto updateItem(ItemUpdateDto itemDto, Long ownerId, Long itemId) throws ItemNotFoundException, NotOwnerException;

    ItemResponseDto getItemById(Long itemId) throws ItemNotFoundException;

    List<ItemResponseDto> getAllItemsInUserOwn(Long userId);

    List<ItemResponseDto> getItemsBySearch(String text);

}
