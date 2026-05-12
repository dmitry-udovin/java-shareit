package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingApproveStatus;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.comment.dto.CommentCreateDto;
import ru.practicum.shareit.comment.dto.CommentResponseDto;
import ru.practicum.shareit.comment.exception.CommentValidationException;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.exception.OwnerNotExistsException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.impl.ItemServiceImpl;
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
class ItemServiceImplIntegrationTest {

    @Autowired
    private ItemServiceImpl itemService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private BookingRepository bookingRepository;

    private User owner;
    private User booker;

    @BeforeEach
    void setUp() {
        owner = userRepository.save(user("Owner", "owner-item@t.test"));
        booker = userRepository.save(user("Booker", "booker-item@t.test"));
    }

    private static User user(String name, String email) {
        User u = new User();
        u.setName(name);
        u.setEmail(email);
        return u;
    }

    @Test
    void saveItem_persistsWithOwner() {
        ItemResponseDto dto = itemService.saveItem(
                new ItemCreateDto("Hammer", "Heavy", true, null),
                owner.getId());

        assertThat(dto.id()).isNotNull();
        assertThat(dto.name()).isEqualTo("Hammer");
        Item stored = itemRepository.findById(dto.id()).orElseThrow();
        assertThat(stored.getOwnerId()).isEqualTo(owner.getId());
    }

    @Test
    void updateItem_changesFields() {
        ItemResponseDto created = itemService.saveItem(
                new ItemCreateDto("Old", "Desc", true, null),
                owner.getId());

        ItemResponseDto updated = itemService.updateItem(
                new ItemUpdateDto("New", "NewDesc", null),
                owner.getId(),
                created.id());

        assertThat(updated.name()).isEqualTo("New");
        assertThat(updated.description()).isEqualTo("NewDesc");
    }

    @Test
    void getItemById_loadsComments() {
        ItemResponseDto item = itemService.saveItem(
                new ItemCreateDto("Thing", "D", true, null),
                owner.getId());
        LocalDateTime pastStart = LocalDateTime.now().minusDays(5);
        LocalDateTime pastEnd = LocalDateTime.now().minusDays(4);
        saveApprovedBooking(item.id(), pastStart, pastEnd);

        CommentResponseDto comment = itemService.addComment(item.id(), booker.getId(),
                new CommentCreateDto("Nice"));

        ItemResponseDto found = itemService.getItemById(item.id());

        assertThat(found.comments()).hasSize(1);
        assertThat(found.comments().get(0).text()).isEqualTo("Nice");
        assertThat(found.comments().get(0).id()).isEqualTo(comment.id());
    }

    @Test
    void getAllItemsInUserOwn_attachesLastAndNextBooking() {
        ItemResponseDto item = itemService.saveItem(
                new ItemCreateDto("Bike", "Fast", true, null),
                owner.getId());
        LocalDateTime now = LocalDateTime.now();
        saveApprovedBooking(item.id(), now.minusDays(10), now.minusDays(9));
        saveApprovedBooking(item.id(), now.plusDays(1), now.plusDays(2));

        List<ItemResponseDto> list = itemService.getAllItemsInUserOwn(owner.getId());

        assertThat(list).hasSize(1);
        assertThat(list.get(0).lastBooking()).isNotNull();
        assertThat(list.get(0).nextBooking()).isNotNull();
    }

    @Test
    void getItemsBySearch_blank_returnsEmpty() {
        itemService.saveItem(new ItemCreateDto("Drill", "Power tool", true, null), owner.getId());

        assertThat(itemService.getItemsBySearch(" ")).isEmpty();
        assertThat(itemService.getItemsBySearch(null)).isEmpty();
    }

    @Test
    void getItemsBySearch_findsByText() {
        itemService.saveItem(new ItemCreateDto("Rare drill", "Rare item", true, null), owner.getId());

        List<ItemResponseDto> found = itemService.getItemsBySearch("rAre");

        assertThat(found).extracting(ItemResponseDto::name).contains("Rare drill");
    }

    @Test
    void getItemById_missing_throws() {
        assertThatThrownBy(() -> itemService.getItemById(999L))
                .isInstanceOf(ItemNotFoundException.class);
    }

    @Test
    void updateItem_unknownOwner_throws() {
        ItemResponseDto created = itemService.saveItem(
                new ItemCreateDto("X", "Y", true, null),
                owner.getId());

        assertThatThrownBy(() -> itemService.updateItem(
                new ItemUpdateDto("Z", "W", null),
                999_999L,
                created.id()))
                .isInstanceOf(OwnerNotExistsException.class);
    }

    @Test
    void addComment_withoutPastBooking_throws() {
        ItemResponseDto item = itemService.saveItem(
                new ItemCreateDto("Thing", "D", true, null),
                owner.getId());

        assertThatThrownBy(() -> itemService.addComment(item.id(), booker.getId(),
                new CommentCreateDto("Bad")))
                .isInstanceOf(CommentValidationException.class);
    }

    @Test
    void getAllItemsInUserOwn_empty_returnsEmpty() {
        User lone = userRepository.save(user("Lone", "lone-empty@t.test"));

        assertThat(itemService.getAllItemsInUserOwn(lone.getId())).isEmpty();
    }

    private void saveApprovedBooking(Long itemId, LocalDateTime start, LocalDateTime end) {
        Item item = itemRepository.findById(itemId).orElseThrow();
        Booking b = new Booking();
        b.setBookedItem(item);
        b.setUserWhoBooked(booker);
        b.setStartRentTime(start);
        b.setEndRentTime(end);
        b.setStatus(BookingApproveStatus.APPROVED);
        bookingRepository.save(b);
    }
}
