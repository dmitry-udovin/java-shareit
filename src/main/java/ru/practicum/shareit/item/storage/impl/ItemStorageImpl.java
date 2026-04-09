package ru.practicum.shareit.item.storage.impl;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ItemStorageImpl implements ItemStorage {

    private Map<Long, Item> items = new HashMap<>();

    @Override
    public Item save(Item item) {
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Item update(Item itemForUpdate) throws ItemNotFoundException {
        if (items.containsKey(itemForUpdate.getId())) {
            items.put(itemForUpdate.getId(), itemForUpdate);
            return itemForUpdate;
        } else {
            throw new ItemNotFoundException("Отсутствует вещь с указанным id для обновления.");
        }
    }

    @Override
    public Item findById(Long itemId) throws ItemNotFoundException {
        if (items.containsKey(itemId)) {
            return items.get(itemId);
        } else {
            throw new ItemNotFoundException("Вещь с указанным номером отсутствует.");
        }
    }

    @Override
    public Item deleteById(Long itemId) throws ItemNotFoundException {
        if (items.containsKey(itemId)) {
            return items.remove(itemId);
        } else {
            throw new ItemNotFoundException("Вещь по указанному номеру отсутствует, невозможно удалить.");
        }
    }

    @Override
    public List<Item> getAllItems() {
        return items.values().stream().toList();
    }

    @Override
    public List<Item> searchByText(String text) {
        return items.values().stream()
                .filter(item -> item.getAvailable() == true)
                .filter(item -> containsIgnoreCase(item.getName(), text) ||
                        containsIgnoreCase(item.getDescription(), text)
                )
                .toList();
    }

    private static boolean containsIgnoreCase(String source, String search) {
        if (source == null || search == null) {
            return false;
        }
        return source.toLowerCase().contains(search.toLowerCase());
    }

}
