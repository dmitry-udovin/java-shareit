package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemStorage {

    Item save(Item item);

    Item update(Item item);

    Item findById(Long itemId);

    Item deleteById(Long itemId);

    List<Item> getAllItems();

    List<Item> searchByText(String text);

}
