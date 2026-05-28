package ru.practicum.shareit.booking.dto;


import java.time.LocalDateTime;

public record BookingShortDto(long id, long bookerId, LocalDateTime start, LocalDateTime end) {

}