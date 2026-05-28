package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.dto.CreateBookingDto;
import ru.practicum.shareit.booking.exception.AccessDeniedException;
import ru.practicum.shareit.booking.exception.CannotCreateBookingException;
import ru.practicum.shareit.booking.exception.MissingBookingException;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingApproveStatus;
import ru.practicum.shareit.booking.service.impl.BookingServiceImpl;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class BookingServiceImplIntegrationTest {

    @Autowired
    private BookingServiceImpl bookingService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private BookingRepository bookingRepository;

    private User owner;
    private User booker;
    private Item item;

    @BeforeEach
    void setUp() {
        owner = saveUser("Owner", "owner-book@t.test");
        booker = saveUser("Booker", "booker-book@t.test");
        item = new Item();
        item.setName("Table");
        item.setDescription("Wood");
        item.setOwnerId(owner.getId());
        item.setAvailable(true);
        item.setCountRents(0);
        item = itemRepository.save(item);
    }

    private User saveUser(String name, String email) {
        User u = new User();
        u.setName(name);
        u.setEmail(email);
        return userRepository.save(u);
    }

    @Test
    void createBooking_savesWaitingStatus() {
        LocalDateTime start = LocalDateTime.now().plusDays(1).withNano(0);
        LocalDateTime end = start.plusDays(1);

        BookingResponseDto dto = bookingService.createBooking(
                new CreateBookingDto(item.getId(), start, end),
                booker.getId());

        assertThat(dto.status()).isEqualTo(BookingApproveStatus.WAITING);
        assertThat(dto.item().id()).isEqualTo(item.getId());
        assertThat(bookingRepository.findById(dto.id())).isPresent();
    }

    @Test
    void createBooking_ownerBooksOwnItem_throws() {
        LocalDateTime start = LocalDateTime.now().plusDays(1).withNano(0);
        LocalDateTime end = start.plusDays(1);

        assertThatThrownBy(() -> bookingService.createBooking(
                new CreateBookingDto(item.getId(), start, end),
                owner.getId()))
                .isInstanceOf(CannotCreateBookingException.class);
    }

    @Test
    void updateBookingStatus_ownerApproves() {
        Booking b = new Booking();
        b.setBookedItem(item);
        b.setUserWhoBooked(booker);
        b.setStartRentTime(LocalDateTime.now().plusDays(1));
        b.setEndRentTime(LocalDateTime.now().plusDays(2));
        b.setStatus(BookingApproveStatus.WAITING);
        b = bookingRepository.save(b);

        BookingResponseDto dto = bookingService.updateBookingStatus(b.getId(), true, owner.getId());

        assertThat(dto.status()).isEqualTo(BookingApproveStatus.APPROVED);
    }

    @Test
    void findBookingById_bookerCanRead() {
        Booking b = new Booking();
        b.setBookedItem(item);
        b.setUserWhoBooked(booker);
        b.setStartRentTime(LocalDateTime.now().plusDays(1));
        b.setEndRentTime(LocalDateTime.now().plusDays(2));
        b.setStatus(BookingApproveStatus.WAITING);
        b = bookingRepository.save(b);

        BookingResponseDto dto = bookingService.findBookingById(b.getId(), booker.getId());

        assertThat(dto.id()).isEqualTo(b.getId());
    }

    @Test
    void findBookingById_stranger_throws() {
        User other = saveUser("Other", "other-book@t.test");
        Booking b = new Booking();
        b.setBookedItem(item);
        b.setUserWhoBooked(booker);
        b.setStartRentTime(LocalDateTime.now().plusDays(1));
        b.setEndRentTime(LocalDateTime.now().plusDays(2));
        b.setStatus(BookingApproveStatus.WAITING);
        b = bookingRepository.save(b);
        long bookingId = b.getId();

        assertThatThrownBy(() -> bookingService.findBookingById(bookingId, other.getId()))
                .isInstanceOf(AccessDeniedException.class);
    }

    @Test
    void findBookingsForUser_filtersByFutureState() {
        Booking past = new Booking();
        past.setBookedItem(item);
        past.setUserWhoBooked(booker);
        past.setStartRentTime(LocalDateTime.now().minusDays(5));
        past.setEndRentTime(LocalDateTime.now().minusDays(4));
        past.setStatus(BookingApproveStatus.APPROVED);
        bookingRepository.save(past);

        Booking future = new Booking();
        future.setBookedItem(item);
        future.setUserWhoBooked(booker);
        future.setStartRentTime(LocalDateTime.now().plusDays(3));
        future.setEndRentTime(LocalDateTime.now().plusDays(4));
        future.setStatus(BookingApproveStatus.APPROVED);
        bookingRepository.save(future);

        List<BookingResponseDto> list = bookingService.findBookingsForUser(booker.getId(), "FUTURE");

        assertThat(list).extracting(BookingResponseDto::id).contains(future.getId());
        assertThat(list).extracting(BookingResponseDto::id).doesNotContain(past.getId());
    }

    @Test
    void findBookingsForOwnerItems_returnsBookingsForItems() {
        Booking b = new Booking();
        b.setBookedItem(item);
        b.setUserWhoBooked(booker);
        b.setStartRentTime(LocalDateTime.now().plusDays(1));
        b.setEndRentTime(LocalDateTime.now().plusDays(2));
        b.setStatus(BookingApproveStatus.WAITING);
        bookingRepository.save(b);

        List<BookingResponseDto> list = bookingService.findBookingsForOwnerItems(owner.getId(), "ALL");

        assertThat(list).hasSize(1);
    }

    @Test
    void updateBookingStatus_notOwner_throws() {
        Booking b = new Booking();
        b.setBookedItem(item);
        b.setUserWhoBooked(booker);
        b.setStartRentTime(LocalDateTime.now().plusDays(1));
        b.setEndRentTime(LocalDateTime.now().plusDays(2));
        b.setStatus(BookingApproveStatus.WAITING);
        b = bookingRepository.save(b);
        long bookingId = b.getId();

        assertThatThrownBy(() -> bookingService.updateBookingStatus(bookingId, true, booker.getId()))
                .isInstanceOf(MissingBookingException.class);
    }

    @Test
    void createBooking_unavailableItem_throws() {
        item.setAvailable(false);
        itemRepository.save(item);
        LocalDateTime start = LocalDateTime.now().plusDays(1).withNano(0);
        LocalDateTime end = start.plusDays(1);

        assertThatThrownBy(() -> bookingService.createBooking(
                new CreateBookingDto(item.getId(), start, end),
                booker.getId()))
                .isInstanceOf(CannotCreateBookingException.class);
    }

    @Test
    void createBooking_unknownItem_throws() {
        LocalDateTime start = LocalDateTime.now().plusDays(1).withNano(0);
        LocalDateTime end = start.plusDays(1);

        assertThatThrownBy(() -> bookingService.createBooking(
                new CreateBookingDto(999_999, start, end),
                booker.getId()))
                .isInstanceOf(ItemNotFoundException.class);
    }

    @Test
    void createBooking_unknownUser_throws() {
        LocalDateTime start = LocalDateTime.now().plusDays(1).withNano(0);
        LocalDateTime end = start.plusDays(1);

        assertThatThrownBy(() -> bookingService.createBooking(
                new CreateBookingDto(item.getId(), start, end),
                999_999L))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void updateBookingStatus_rejects_setsCancelled() {
        Booking b = new Booking();
        b.setBookedItem(item);
        b.setUserWhoBooked(booker);
        b.setStartRentTime(LocalDateTime.now().plusDays(1));
        b.setEndRentTime(LocalDateTime.now().plusDays(2));
        b.setStatus(BookingApproveStatus.WAITING);
        b = bookingRepository.save(b);

        BookingResponseDto dto = bookingService.updateBookingStatus(b.getId(), false, owner.getId());

        assertThat(dto.status()).isEqualTo(BookingApproveStatus.CANCELLED);
    }

    @Test
    void findBookingById_ownerCanRead() {
        Booking b = new Booking();
        b.setBookedItem(item);
        b.setUserWhoBooked(booker);
        b.setStartRentTime(LocalDateTime.now().plusDays(1));
        b.setEndRentTime(LocalDateTime.now().plusDays(2));
        b.setStatus(BookingApproveStatus.WAITING);
        b = bookingRepository.save(b);

        BookingResponseDto dto = bookingService.findBookingById(b.getId(), owner.getId());

        assertThat(dto.id()).isEqualTo(b.getId());
    }

    @Test
    void findBookingsForUser_unknownState_throws() {
        assertThatThrownBy(() -> bookingService.findBookingsForUser(booker.getId(), "NOPE"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void findBookingsForUser_unknownUser_throws() {
        assertThatThrownBy(() -> bookingService.findBookingsForUser(999_999L, "ALL"))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void findBookingsForOwnerItems_unknownUser_throws() {
        assertThatThrownBy(() -> bookingService.findBookingsForOwnerItems(999_999L, "ALL"))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void findBookingsForUser_filtersCurrent() {
        Booking b = new Booking();
        b.setBookedItem(item);
        b.setUserWhoBooked(booker);
        LocalDateTime now = LocalDateTime.now();
        b.setStartRentTime(now.minusHours(1));
        b.setEndRentTime(now.plusHours(1));
        b.setStatus(BookingApproveStatus.APPROVED);
        bookingRepository.save(b);

        List<BookingResponseDto> list = bookingService.findBookingsForUser(booker.getId(), "CURRENT");

        assertThat(list).extracting(BookingResponseDto::id).contains(b.getId());
    }

    @Test
    void findBookingsForUser_filtersPast() {
        Booking b = new Booking();
        b.setBookedItem(item);
        b.setUserWhoBooked(booker);
        b.setStartRentTime(LocalDateTime.now().minusDays(10));
        b.setEndRentTime(LocalDateTime.now().minusDays(9));
        b.setStatus(BookingApproveStatus.APPROVED);
        bookingRepository.save(b);

        List<BookingResponseDto> list = bookingService.findBookingsForUser(booker.getId(), "PAST");

        assertThat(list).extracting(BookingResponseDto::id).contains(b.getId());
    }

    @Test
    void findBookingsForUser_filtersWaiting() {
        Booking b = new Booking();
        b.setBookedItem(item);
        b.setUserWhoBooked(booker);
        b.setStartRentTime(LocalDateTime.now().plusDays(1));
        b.setEndRentTime(LocalDateTime.now().plusDays(2));
        b.setStatus(BookingApproveStatus.WAITING);
        bookingRepository.save(b);

        List<BookingResponseDto> list = bookingService.findBookingsForUser(booker.getId(), "WAITING");

        assertThat(list).extracting(BookingResponseDto::id).contains(b.getId());
    }

    @Test
    void findBookingsForUser_filtersRejected() {
        Booking b = new Booking();
        b.setBookedItem(item);
        b.setUserWhoBooked(booker);
        b.setStartRentTime(LocalDateTime.now().plusDays(1));
        b.setEndRentTime(LocalDateTime.now().plusDays(2));
        b.setStatus(BookingApproveStatus.CANCELLED);
        bookingRepository.save(b);

        List<BookingResponseDto> list = bookingService.findBookingsForUser(booker.getId(), "REJECTED");

        assertThat(list).extracting(BookingResponseDto::id).contains(b.getId());
    }

    @Test
    void findBookingsForOwnerItems_filtersByState() {
        Booking b = new Booking();
        b.setBookedItem(item);
        b.setUserWhoBooked(booker);
        b.setStartRentTime(LocalDateTime.now().plusDays(3));
        b.setEndRentTime(LocalDateTime.now().plusDays(4));
        b.setStatus(BookingApproveStatus.APPROVED);
        bookingRepository.save(b);

        List<BookingResponseDto> list = bookingService.findBookingsForOwnerItems(owner.getId(), "FUTURE");

        assertThat(list).extracting(BookingResponseDto::id).contains(b.getId());
    }
}
