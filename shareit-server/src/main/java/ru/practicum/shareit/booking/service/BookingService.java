package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.dto.CreateBookingDto;

import java.util.List;

public interface BookingService {

    BookingResponseDto createBooking(CreateBookingDto dto, Long userId);

    BookingResponseDto updateBookingStatus(Long bookingId, Boolean approved, Long ownerId);

    BookingResponseDto findBookingById(Long bookingId, Long userId);

    List<BookingResponseDto> findBookingsForUser(Long userId, String state);

    List<BookingResponseDto> findBookingsForOwnerItems(Long ownerId, String state);

}
