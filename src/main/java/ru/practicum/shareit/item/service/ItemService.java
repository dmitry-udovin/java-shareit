package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.dto.ManyItemsResponseDto;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.user.exception.NotOwnerException;
import ru.practicum.shareit.user.exception.UserNotFoundException;

import java.util.List;

public interface ItemService {

    ItemResponseDto saveItem(ItemCreateDto createDto, Long ownerId) throws UserNotFoundException;

    ItemResponseDto updateItem(ItemDto itemDto, Long ownerId, Long itemId) throws ItemNotFoundException, NotOwnerException;

    ItemResponseDto getItemById(Long itemId) throws ItemNotFoundException;

    List<ManyItemsResponseDto> getAllItemsInUserOwn(Long userId);

    List<ManyItemsResponseDto> getItemsBySearch(String text);

}
