package ru.practicum.shareit.item.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.dto.ManyItemsResponseDto;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.exception.OwnerNotExistsException;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.exception.NotOwnerException;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemStorage itemStorage;
    private long counter = 1;

    private final UserStorage userStorage;

    @Override
    public ItemResponseDto saveItem(ItemCreateDto createDto, Long ownerId) throws UserNotFoundException {

        Item item = ItemMapper.itemDtoToItem(createDto);

        item.setId(counter);
        counter++;

        try {
            User owner = userStorage.findById(ownerId);
            item.setOwnerId(ownerId);
        } catch (UserNotFoundException exp) {
            throw new UserNotFoundException("Невозможно добавить вещь пользователю которого нет.");
        }

        Item savedItem = itemStorage.save(item);

        return ItemMapper.itemToResponseDto(savedItem);
    }

    @Override
    public ItemResponseDto updateItem(ItemUpdateDto itemDto, Long ownerId, Long itemId) throws ItemNotFoundException, NotOwnerException {

        Item item = itemStorage.findById(itemId);

        ItemMapper.updateItemFromDto(itemDto, item);

        try {
            userStorage.findById(ownerId);
        } catch (UserNotFoundException exp) {
            throw new OwnerNotExistsException("Невозможно обновить данные, пользователя не существует.");
        }

        item.setId(itemId);
        item.setOwnerId(ownerId);
        Item updatedItem = itemStorage.update(item);

        return ItemMapper.itemToResponseDto(updatedItem);
    }

    @Override
    public ItemResponseDto getItemById(Long itemId) throws ItemNotFoundException {

        return ItemMapper.itemToResponseDto(itemStorage.findById(itemId));
    }

    @Override
    public List<ManyItemsResponseDto> getAllItemsInUserOwn(Long userId) {
        return itemStorage.getAllItems().stream()
                .filter(item -> userId.equals(item.getOwnerId()))
                .map(ItemMapper::itemToManyResponseDto)
                .toList();
    }

    @Override
    public List<ManyItemsResponseDto> getItemsBySearch(String text) {

        if (text == null || text.isBlank()) {
            return List.of();
        }

        return itemStorage.searchByText(text).stream()
                .map(ItemMapper::itemToManyResponseDto)
                .toList();
    }


}
