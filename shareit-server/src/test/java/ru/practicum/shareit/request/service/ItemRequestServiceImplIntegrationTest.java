package ru.practicum.shareit.request.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.service.impl.ItemServiceImpl;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.exception.RequestNotFoundException;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.impl.ItemRequestServiceImpl;
import ru.practicum.shareit.request.storage.ItemRequestRepository;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ItemRequestServiceImplIntegrationTest {

    @Autowired
    private ItemRequestServiceImpl itemRequestService;

    @Autowired
    private ItemServiceImpl itemService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRequestRepository itemRequestRepository;

    private User requester;
    private User other;

    @BeforeEach
    void setUp() {
        requester = saveUser("Req", "req@t.test");
        other = saveUser("Oth", "oth@t.test");
    }

    private User saveUser(String name, String email) {
        User u = new User();
        u.setName(name);
        u.setEmail(email);
        return userRepository.save(u);
    }

    @Test
    void create_persistsRequest() {
        ItemRequestResponseDto dto = itemRequestService.create(
                requester.getId(),
                new ItemRequestCreateDto("Need a ladder"));

        assertThat(dto.id()).isNotNull();
        assertThat(dto.description()).isEqualTo("Need a ladder");
        assertThat(dto.requesterId()).isEqualTo(requester.getId());
        assertThat(dto.items()).isEmpty();
    }

    @Test
    void getOwnerRequests_includesItemsCreatedForRequest() {
        ItemRequestResponseDto req = itemRequestService.create(
                requester.getId(),
                new ItemRequestCreateDto("Need drill"));
        itemService.saveItem(
                new ItemCreateDto("Drill", "Cordless", true, req.id()),
                other.getId());

        List<ItemRequestResponseDto> list = itemRequestService.getOwnerRequests(requester.getId());

        assertThat(list).hasSize(1);
        assertThat(list.get(0).items()).hasSize(1);
        assertThat(list.get(0).items().get(0).name()).isEqualTo("Drill");
    }

    @Test
    void getAllRequests_excludesOwn_andPaginates() {
        itemRequestService.create(other.getId(), new ItemRequestCreateDto("From other"));
        itemRequestService.create(other.getId(), new ItemRequestCreateDto("From other 2"));

        List<ItemRequestResponseDto> page = itemRequestService.getAllRequests(requester.getId(), 0, 1);

        assertThat(page).hasSize(1);
    }

    @Test
    void getRequestById_returnsRequestWithItems() {
        ItemRequestResponseDto req = itemRequestService.create(
                requester.getId(),
                new ItemRequestCreateDto("Need saw"));
        itemService.saveItem(
                new ItemCreateDto("Saw", "Sharp", true, req.id()),
                other.getId());

        ItemRequestResponseDto found = itemRequestService.getRequestById(req.id(), requester.getId());

        assertThat(found.items()).hasSize(1);
    }

    @Test
    void getRequestById_unknown_throws() {
        assertThatThrownBy(() -> itemRequestService.getRequestById(999L, requester.getId()))
                .isInstanceOf(RequestNotFoundException.class);
    }

    @Test
    void getAllRequests_invalidPagination_throws() {
        assertThatThrownBy(() -> itemRequestService.getAllRequests(requester.getId(), -1, 10))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void getAllRequests_zeroSize_throws() {
        assertThatThrownBy(() -> itemRequestService.getAllRequests(requester.getId(), 0, 0))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void getRequestById_unknownUser_throws() {
        assertThatThrownBy(() -> itemRequestService.getRequestById(1L, 999_999L))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void itemRequest_entityPersistAndLoad() {
        ItemRequest ir = new ItemRequest();
        ir.setDescription("d");
        ir.setRequester(requester);
        ir.setCreated(LocalDateTime.now());
        ItemRequest saved = itemRequestRepository.save(ir);
        ItemRequest loaded = itemRequestRepository.findById(saved.getId()).orElseThrow();
        assertThat(loaded.getDescription()).isEqualTo("d");
        assertThat(loaded.getItems()).isNotNull();
    }
}
