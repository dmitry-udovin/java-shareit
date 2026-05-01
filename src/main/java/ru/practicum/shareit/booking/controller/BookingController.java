package ru.practicum.shareit.booking.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.dto.CreateBookingDto;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;


@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(path = "/bookings")
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public BookingResponseDto createNewBookingRequest(@RequestBody @Valid CreateBookingDto createDto,
                                                      @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Получен запрос на создание бронирования: {}", createDto);

        return bookingService.createBooking(createDto, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingResponseDto updateBookingStatusFromOwner(@PathVariable Long bookingId,
                                                           @RequestParam Boolean approved,
                                                           @RequestHeader("X-Sharer-User-Id") Long ownerId) {
        log.info("Запрос на обновление бронирования: bookingId={}, approved={}, userId={}",
                bookingId, approved, ownerId);

        return bookingService.updateBookingStatus(bookingId, approved, ownerId);
    }

    @GetMapping("/{bookingId}")
    public BookingResponseDto getBooking(
            @PathVariable Long bookingId,
            @RequestHeader("X-Sharer-User-Id") Long userId) {
        // автор или владелец вещи
        log.info("Получен запрос на получение данных о бронировании: bookingId={}, userId={}",
                bookingId, userId);

        return bookingService.findBookingById(bookingId, userId);
    }

    @GetMapping()
    public List<BookingResponseDto> getBookingsForUser(
            // бронирования конкретного пользователя
            @RequestParam(defaultValue = "ALL") String state,
            @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Получен запрос на получение списка бронирований пользователя, state={}", state);

        return bookingService.findBookingsForUser(userId, state);
    }

    @GetMapping("/owner")
    public List<BookingResponseDto> getBookingsForItemOwner(
            @RequestParam(defaultValue = "ALL") String state,
            @RequestHeader("X-Sharer-User-Id") Long ownerId) {
        log.info("Получен запрос на получение бронирований вещей пользователя (владельца), state={}", state);

        return bookingService.findBookingsForOwnerItems(ownerId, state);
    }

}
