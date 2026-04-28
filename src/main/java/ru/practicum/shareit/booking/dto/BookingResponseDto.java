package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.booking.model.BookingApproveStatus;
import ru.practicum.shareit.item.dto.ItemShortDto;
import ru.practicum.shareit.user.dto.UserShortDto;

import java.time.LocalDateTime;

public record BookingResponseDto(Long id, LocalDateTime start, LocalDateTime end, BookingApproveStatus status,
                                 UserShortDto booker, ItemShortDto item) {

}