package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemStorage {

    Item save(Item item);

    Item update(Item item) throws ItemNotFoundException;

    Item findById(Long itemId) throws ItemNotFoundException;

    Item deleteById(Long itemId) throws ItemNotFoundException;

    List<Item> getAllItems();

    List<Item> searchByText(String text);

}
