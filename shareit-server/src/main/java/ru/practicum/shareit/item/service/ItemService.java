package ru.practicum.shareit.item.service;

import ru.practicum.shareit.comment.dto.CommentCreateDto;
import ru.practicum.shareit.comment.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;

import java.util.List;

public interface ItemService {

    ItemResponseDto saveItem(ItemCreateDto createDto, long ownerId);

    ItemResponseDto updateItem(ItemUpdateDto itemDto, long ownerId, long itemId);

    ItemResponseDto getItemById(long itemId);

    List<ItemResponseDto> getAllItemsInUserOwn(long userId);

    List<ItemResponseDto> getItemsBySearch(String text);

    CommentResponseDto addComment(long itemId, long userId, CommentCreateDto dto);

}
