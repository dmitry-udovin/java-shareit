package ru.practicum.shareit.booking.mapper;

import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ItemShortDto;
import ru.practicum.shareit.user.dto.UserShortDto;

public class BookingMapper {

    private BookingMapper() {

    }

    public static BookingResponseDto toResponseDto(Booking booking) {

        BookingResponseDto dto = new BookingResponseDto();
        dto.setId(booking.getId());
        dto.setStart(booking.getStartRentTime());
        dto.setEnd(booking.getEndRentTime());
        dto.setStatus(booking.getStatus());

        // Маппим booker
        UserShortDto bookerDto = new UserShortDto();
        bookerDto.setId(booking.getUserWhoBooked().getId());
        dto.setBooker(bookerDto);

        // Маппим item
        ItemShortDto itemDto = new ItemShortDto();
        itemDto.setId(booking.getBookedItem().getId());
        itemDto.setName(booking.getBookedItem().getName());
        dto.setItem(itemDto);

        return dto;
    }

    public static BookingShortDto toShortDto(Booking booking) {
        if (booking == null) return null;
        return new BookingShortDto(
                booking.getId(),
                booking.getUserWhoBooked().getId(),
                booking.getStartRentTime(),
                booking.getEndRentTime()
        );
    }


}
