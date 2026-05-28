package ru.practicum.shareit.item.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingApproveStatus;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.comment.dto.CommentCreateDto;
import ru.practicum.shareit.comment.dto.CommentResponseDto;
import ru.practicum.shareit.comment.exception.CommentValidationException;
import ru.practicum.shareit.comment.mapper.CommentMapper;
import ru.practicum.shareit.comment.storage.CommentRepository;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.exception.OwnerNotExistsException;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.request.exception.RequestNotFoundException;
import ru.practicum.shareit.request.storage.ItemRequestRepository;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final ItemRequestRepository requestRepository;

    @Override
    public ItemResponseDto saveItem(ItemCreateDto createDto, long ownerId) {

        Item item = ItemMapper.itemDtoToItem(createDto);

        if (createDto.requestId() != null) {
            var request = requestRepository.findById(createDto.requestId())
                    .orElseThrow(() -> new RequestNotFoundException("Запрос на вещь не найден"));
            item.setRequest(request);
        }

        userRepository.findById(ownerId).orElseThrow(() ->
                new UserNotFoundException("Невозможно добавить вещь пользователю которого нет."));
        item.setOwnerId(ownerId);

        Item savedItem = itemRepository.save(item);

        ItemResponseDto dto = ItemMapper.itemToResponseDto(savedItem);

        return dto.withComments(Collections.emptyList());
    }

    @Override
    public ItemResponseDto updateItem(ItemUpdateDto itemDto, long ownerId, long itemId) {

        Item item = itemRepository.findById(itemId).orElseThrow(() ->
                new ItemNotFoundException("Вещь с указанным номером отсутствует."));

        userRepository.findById(ownerId).orElseThrow(() ->
                new OwnerNotExistsException("Невозможно обновить данные, пользователя не существует."));

        ItemMapper.updateItemFromDto(itemDto, item);


        item.setId(itemId);
        item.setOwnerId(ownerId);
        Item updatedItem = itemRepository.save(item);

        return ItemMapper.itemToResponseDto(updatedItem);
    }

    @Override
    @Transactional(readOnly = true)
    public ItemResponseDto getItemById(long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException("Вещь с указанным номером отсутствует."));

        ItemResponseDto dto = ItemMapper.itemToResponseDto(item);

        List<CommentResponseDto> comments = commentRepository.findByItem_Id(itemId)
                .stream()
                .map(CommentMapper::toResponseDto)
                .toList();

        return dto.withComments(comments);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemResponseDto> getAllItemsInUserOwn(long userId) {

        List<Item> items = itemRepository.findByOwnerId(userId);
        if (items.isEmpty()) {
            return List.of();
        }

        List<Long> itemIds = items.stream().map(Item::getId).toList();

        List<Booking> bookings = bookingRepository.findApprovedByItemIds(itemIds, BookingApproveStatus.APPROVED);

        Map<Long, List<Booking>> bookingsByItem = bookings.stream()
                .collect(Collectors.groupingBy(b -> b.getBookedItem().getId()));

        LocalDateTime now = LocalDateTime.now();


        return items.stream().map(item -> {
            ItemResponseDto dto = ItemMapper.itemToResponseDto(item);
            List<Booking> itemBookings = bookingsByItem.getOrDefault(item.getId(), Collections.emptyList());

            Booking next = itemBookings.stream()
                    .filter(b -> b.getStartRentTime().isAfter(now))
                    .min(Comparator.comparing(Booking::getStartRentTime))
                    .orElse(null);

            Booking last = itemBookings.stream()
                    .filter(b -> b.getEndRentTime().isBefore(now))
                    .max(Comparator.comparing(Booking::getEndRentTime))
                    .orElse(null);

            if (next != null) {
                dto = dto.withNextBooking(BookingMapper.toShortDto(next));
            }
            if (last != null) {
                dto = dto.withLastBooking(BookingMapper.toShortDto(last));
            }

            return dto;
        }).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemResponseDto> getItemsBySearch(String text) {
        if (text == null || text.isBlank()) {
            return List.of();
        }

        return itemRepository.searchByText(text).stream()
                .map(ItemMapper::itemToResponseDto)
                .toList();
    }

    @Override
    public CommentResponseDto addComment(long itemId, long userId, CommentCreateDto dto) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException("Вещь с id=" + itemId + " не найдена"));

        User author = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с id=" + userId + " не найден"));

        bookingRepository.findApprovedPastBooking(itemId, userId, LocalDateTime.now().plusSeconds(1))
                .orElseThrow(() -> new CommentValidationException(
                        "Нельзя оставить отзыв: нет завершённого одобренного бронирования этой вещи"));

        Comment comment = new Comment();
        comment.setText(dto.text());
        comment.setItem(item);
        comment.setWhoCommented(author);
        comment.setDateOfCreate(LocalDateTime.now());

        Comment saved = commentRepository.save(comment);
        return CommentMapper.toResponseDto(saved);
    }

}