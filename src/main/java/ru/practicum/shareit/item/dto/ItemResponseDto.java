package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.comment.dto.CommentResponseDto;

import java.util.List;

public record ItemResponseDto(Long id, String name, String description, Boolean available, BookingShortDto lastBooking,
                              BookingShortDto nextBooking, List<CommentResponseDto> comments) {

    public ItemResponseDto(Long id, String name, String description, Boolean available) {
        this(id, name, description, available, null, null, null);
    }

    public ItemResponseDto withComments(List<CommentResponseDto> newComments) {
        return new ItemResponseDto(this.id, this.name, this.description, this.available,
                this.lastBooking, this.nextBooking, newComments);
    }

    public ItemResponseDto withLastBooking(BookingShortDto newLastBooking) {
        return new ItemResponseDto(this.id, this.name, this.description, this.available,
                newLastBooking, this.nextBooking, this.comments);
    }

    public ItemResponseDto withNextBooking(BookingShortDto newNextBooking) {
        return new ItemResponseDto(this.id, this.name, this.description, this.available,
                this.lastBooking, newNextBooking, this.comments);
    }

}