package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.dto.ManyItemsResponseDto;
import ru.practicum.shareit.item.model.Item;

public class ItemMapper {

    public static void updateItemFromDto(ItemUpdateDto itemDto, Item item) {
        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }

    }

    public static Item itemDtoToItem(ItemCreateDto itemCreateDto) {
        Item item = new Item();

        item.setName(itemCreateDto.getName());
        item.setDescription(itemCreateDto.getDescription());
        item.setAvailable(itemCreateDto.getAvailable());

        item.setCountRents(0);

        return item;
    }

    public static ItemResponseDto itemToResponseDto(Item item) {
        ItemResponseDto itemResponseDto = new ItemResponseDto();

        itemResponseDto.setId(item.getId());

        itemResponseDto.setName(item.getName());
        itemResponseDto.setDescription(item.getDescription());
        itemResponseDto.setAvailable(item.getAvailable());

        return itemResponseDto;
    }

    public static ManyItemsResponseDto itemToManyResponseDto(Item item) {
        ManyItemsResponseDto manyItemsResponseDto = new ManyItemsResponseDto();

        manyItemsResponseDto.setName(item.getName());
        manyItemsResponseDto.setDescription(item.getDescription());
        manyItemsResponseDto.setAvailable(item.getAvailable());

        return manyItemsResponseDto;
    }

}
