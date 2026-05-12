package ru.practicum.shareit.booking.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.dto.CreateBookingDto;
import ru.practicum.shareit.booking.exception.AccessDeniedException;
import ru.practicum.shareit.booking.exception.CannotCreateBookingException;
import ru.practicum.shareit.booking.exception.MissingBookingException;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingApproveStatus;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public BookingResponseDto createBooking(CreateBookingDto dto, Long userId) {

        Item item = itemRepository.findById(dto.itemId())
                .orElseThrow(() -> new ItemNotFoundException("Вещь с id=" + dto.itemId() + " не найдена"));


        User booker = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с id=" + userId + " не найден"));

        if (item != null && item.getOwnerId().equals(userId)) {
            throw new CannotCreateBookingException("Вы не можете забронировать вещь у самого себя");
        }

        if (!item.getAvailable()) {
            throw new CannotCreateBookingException("Вы не можете забронировать вещь у самого себя");
        }

        Booking booking = new Booking();
        booking.setBookedItem(item);
        booking.setUserWhoBooked(booker);
        booking.setStartRentTime(dto.start());
        booking.setEndRentTime(dto.end());
        booking.setStatus(BookingApproveStatus.WAITING);

        return BookingMapper.toResponseDto(bookingRepository.save(booking));
    }

    @Override
    public BookingResponseDto updateBookingStatus(Long bookingId, Boolean approved, Long ownerId) {

        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() ->
                new MissingBookingException("Заказа на бронирование с номером " + bookingId + " не существует"));


        if (!booking.getBookedItem().getOwnerId().equals(ownerId)) {
            throw new MissingBookingException("Бронирование не найдено");
        }

        booking.setStatus(approved ? BookingApproveStatus.APPROVED : BookingApproveStatus.CANCELLED);

        return BookingMapper.toResponseDto(bookingRepository.save(booking));
    }

    @Override
    @Transactional(readOnly = true)
    public BookingResponseDto findBookingById(Long bookingId, Long userId) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new MissingBookingException(
                        "Заказ на бронирование с номером " + bookingId + " не существует"));

        boolean isBooker = booking.getUserWhoBooked().getId().equals(userId);
        boolean isOwner = booking.getBookedItem().getOwnerId().equals(userId);

        if (!isBooker && !isOwner) {
            throw new AccessDeniedException("Нет прав на просмотр данного бронирования");
        }

        return BookingMapper.toResponseDto(booking);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingResponseDto> findBookingsForUser(Long userId, String state) {

        userRepository.findById(userId).orElseThrow(() ->
                new UserNotFoundException("Пользователь с id=" + userId + " не найден"));

        State stateEnum = convertToStateFromString(state);

        List<Booking> bookings = bookingRepository.findByUserIdOrderByStartDesc(userId);

        LocalDateTime now = LocalDateTime.now();

        return bookings.stream()
                .filter(b -> matchesState(b, stateEnum, now))
                .map(BookingMapper::toResponseDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingResponseDto> findBookingsForOwnerItems(Long ownerId, String state) {

        userRepository.findById(ownerId).orElseThrow(() ->
                new UserNotFoundException("Пользователь с id=" + ownerId + " не найден"));

        State stateEnum = convertToStateFromString(state);

        List<Booking> bookings = bookingRepository.findByOwnerIdOrderByStartDesc(ownerId);

        LocalDateTime now = LocalDateTime.now();

        return bookings.stream()
                .filter(b -> matchesState(b, stateEnum, now))
                .map(BookingMapper::toResponseDto)
                .toList();
    }

    private boolean matchesState(Booking booking, State state, LocalDateTime now) {
        return switch (state) {
            case ALL -> true;
            case CURRENT -> booking.getStartRentTime().isBefore(now) && booking.getEndRentTime().isAfter(now);
            case PAST -> booking.getEndRentTime().isBefore(now);
            case FUTURE -> booking.getStartRentTime().isAfter(now);
            case WAITING -> booking.getStatus() == BookingApproveStatus.WAITING;
            case REJECTED -> booking.getStatus() == BookingApproveStatus.CANCELLED;
            default -> throw new IllegalStateException("Необработанный статус: " + state);
        };
    }

    private State convertToStateFromString(String state) {
        try {
            return State.valueOf(state.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Неизвестный статус бронирования: " + state);
        }
    }

}
