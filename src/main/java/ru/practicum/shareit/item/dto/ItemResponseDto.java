package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.comment.dto.CommentResponseDto;

import java.util.List;

public record ItemResponseDto(Long id, String name, String description, Boolean available, BookingShortDto lastBooking,
                              BookingShortDto nextBooking, List<CommentResponseDto> comments) {

    public ItemResponseDto(Long id, String name, String description, Boolean available) {
        this(id, name, description, available, null, null, null);
    }

}