package ru.practicum.shareit.item.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.exception.OwnerNotExistsException;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private long counter = 1;

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public ItemResponseDto saveItem(ItemCreateDto createDto, Long ownerId) {

        Item item = ItemMapper.itemDtoToItem(createDto);

        item.setId(counter);
        counter++;

        try {
            userRepository.findById(ownerId);
            item.setOwnerId(ownerId);
        } catch (UserNotFoundException exp) {
            throw new UserNotFoundException("Невозможно добавить вещь пользователю которого нет.");
        }

        Item savedItem = itemRepository.save(item);

        return ItemMapper.itemToResponseDto(savedItem);
    }

    @Override
    public ItemResponseDto updateItem(ItemUpdateDto itemDto, Long ownerId, Long itemId) {

        Item item = itemRepository.findById(itemId).orElseThrow(() ->
                new ItemNotFoundException("Вещь с указанным номером отсутствует."));

        ItemMapper.updateItemFromDto(itemDto, item);

        try {
            userRepository.findById(ownerId);
        } catch (UserNotFoundException exp) {
            throw new OwnerNotExistsException("Невозможно обновить данные, пользователя не существует.");
        }

        item.setId(itemId);
        item.setOwnerId(ownerId);
        Item updatedItem = itemRepository.save(item);

        return ItemMapper.itemToResponseDto(updatedItem);
    }

    @Override
    public ItemResponseDto getItemById(Long itemId) {

        return ItemMapper.itemToResponseDto(itemRepository.findById(itemId).orElseThrow(() ->
                new ItemNotFoundException("Вещь с указанным номером отсутствует.")));
    }

    @Override
    public List<ItemResponseDto> getAllItemsInUserOwn(Long userId) {
        return itemRepository.findAll().stream()
                .filter(item -> userId.equals(item.getOwnerId()))
                .map(ItemMapper::itemToResponseDto)
                .toList();
    }

    @Override
    public List<ItemResponseDto> getItemsBySearch(String text) {

        if (text == null || text.isBlank()) {
            return List.of();
        }

        return itemRepository.searchByText(text).stream()
                .map(ItemMapper::itemToResponseDto)
                .toList();
    }


}
