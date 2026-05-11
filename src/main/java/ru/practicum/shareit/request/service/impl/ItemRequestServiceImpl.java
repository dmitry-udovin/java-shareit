package ru.practicum.shareit.request.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.exception.RequestNotFoundException;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.storage.ItemRequestRepository;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    @Transactional
    public ItemRequestResponseDto create(Long userId, ItemRequestCreateDto dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с id " + userId + " не найден"));

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setDescription(dto.description());
        itemRequest.setRequester(user);
        itemRequest.setCreated(LocalDateTime.now());

        ItemRequest savedRequest = itemRequestRepository.save(itemRequest);
        log.info("Сохранён запрос на вещь {} для пользователя {}", savedRequest.getId(), userId);

        return ItemRequestMapper.toResponseDto(savedRequest);
    }

    @Override
    public List<ItemRequestResponseDto> getOwnerRequests(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("Пользователь с id " + userId + " не найден");
        }

        List<ItemRequest> requests = itemRequestRepository.findByRequesterIdOrderByCreatedDesc(userId);
        Map<Long, List<Item>> itemsByRequestId = collectItemsByRequestId(requests);

        log.info("Получено {} item requests для пользователя {}", requests.size(), userId);
        return requests.stream()
                .map(request -> ItemRequestMapper.toResponseDtoWithItems(
                        request,
                        itemsByRequestId.getOrDefault(request.getId(), Collections.emptyList())
                ))
                .toList();
    }

    @Override
    public List<ItemRequestResponseDto> getAllRequests(Long userId, Integer from, Integer size) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("Пользователь с id " + userId + " не найден");
        }

        if (from < 0 || size < 1) {
            throw new IllegalArgumentException("Неверные параметры пагинации");
        }

        List<ItemRequest> requests = itemRequestRepository.findByRequesterIdNotOrderByCreatedDesc(
                userId,
                PageRequest.of(from / size, size)
        );
        Map<Long, List<Item>> itemsByRequestId = collectItemsByRequestId(requests);

        log.info("Получено {} item requests для пользователя {}", requests.size(), userId);
        return requests.stream()
                .map(request -> ItemRequestMapper.toResponseDtoWithItems(
                        request,
                        itemsByRequestId.getOrDefault(request.getId(), Collections.emptyList())
                ))
                .toList();
    }

    @Override
    public ItemRequestResponseDto getRequestById(Long requestId, Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("Пользователь с id " + userId + " не найден");
        }

        ItemRequest request = itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new RequestNotFoundException("Запрос на вещь с номером " + requestId + " не найден"));
        List<Item> requestItems = itemRepository.findByRequestIdOrderByIdAsc(requestId);

        log.info("Получено item request {} для пользователя {}", requestId, userId);
        return ItemRequestMapper.toResponseDtoWithItems(request, requestItems);
    }

    private Map<Long, List<Item>> collectItemsByRequestId(List<ItemRequest> requests) {
        List<Long> requestIds = requests.stream()
                .map(ItemRequest::getId)
                .toList();
        if (requestIds.isEmpty()) {
            return Collections.emptyMap();
        }

        return itemRepository.findByRequestIdInOrderByIdAsc(requestIds).stream()
                .filter(item -> item.getRequest() != null)
                .collect(Collectors.groupingBy(item -> item.getRequest().getId(), Collectors.mapping(Function.identity(), Collectors.toList())));
    }
}