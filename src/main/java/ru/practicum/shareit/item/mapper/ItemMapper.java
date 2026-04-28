package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.model.Item;

public class ItemMapper {

    private ItemMapper() {

    }

    public static void updateItemFromDto(ItemUpdateDto itemDto, Item item) {
        if (itemDto.name() != null) {
            item.setName(itemDto.name());
        }
        if (itemDto.description() != null) {
            item.setDescription(itemDto.description());
        }
        if (itemDto.available() != null) {
            item.setAvailable(itemDto.available());
        }

    }

    public static Item itemDtoToItem(ItemCreateDto itemCreateDto) {
        Item item = new Item();

        item.setName(itemCreateDto.name());
        item.setDescription(itemCreateDto.description());
        item.setAvailable(itemCreateDto.available());

        item.setCountRents(0);

        return item;
    }

    public static ItemResponseDto itemToResponseDto(Item item) {

        return new ItemResponseDto(item.getId(), item.getName(), item.getDescription(),
                item.getAvailable());
    }

}
