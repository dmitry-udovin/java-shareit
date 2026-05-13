package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.dto.CreateBookingDto;

import java.util.List;

public interface BookingService {

    BookingResponseDto createBooking(CreateBookingDto dto, long userId);

    BookingResponseDto updateBookingStatus(long bookingId, boolean approved, long ownerId);

    BookingResponseDto findBookingById(long bookingId, long userId);

    List<BookingResponseDto> findBookingsForUser(long userId, String state);

    List<BookingResponseDto> findBookingsForOwnerItems(long ownerId, String state);

}
