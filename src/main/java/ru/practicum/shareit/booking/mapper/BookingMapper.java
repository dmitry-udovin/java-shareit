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

        // Маппим booker
        UserShortDto bookerDto = new UserShortDto(booking.getUserWhoBooked().getId());

        // Маппим item
        ItemShortDto itemDto = new ItemShortDto(booking.getBookedItem().getId(), booking.getBookedItem().getName());


        return new BookingResponseDto(booking.getId(), booking.getStartRentTime(), booking.getEndRentTime(),
                booking.getStatus(), bookerDto, itemDto);
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
