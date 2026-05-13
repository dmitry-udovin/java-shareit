package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;

import java.util.List;

public interface ItemRequestService {

    ItemRequestResponseDto create(long userId, ItemRequestCreateDto dto);

    List<ItemRequestResponseDto> getOwnerRequests(long userId);

    List<ItemRequestResponseDto> getAllRequests(long userId, int from, int size);

    ItemRequestResponseDto getRequestById(long requestId, long userId);
}