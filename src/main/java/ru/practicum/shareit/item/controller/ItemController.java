package ru.practicum.shareit.item.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.dto.ManyItemsResponseDto;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.exception.NotOwnerException;
import ru.practicum.shareit.user.exception.UserNotFoundException;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public ItemResponseDto createItem(@Valid @RequestBody ItemCreateDto itemDto,
                                      @RequestHeader("X-Sharer-User-Id") Long userId) throws UserNotFoundException {
        return itemService.saveItem(itemDto, userId);
    }

    @PatchMapping("/{itemId}")
    public ItemResponseDto updateItem(@Valid @PathVariable Long itemId,
                                      @RequestBody ItemDto itemDto,
                                      @RequestHeader("X-Sharer-User-Id") Long userId) throws NotOwnerException, ItemNotFoundException {
        return itemService.updateItem(itemDto, userId, itemId);
    }

    @GetMapping("/{itemId}")
    public ItemResponseDto getById(@PathVariable Long itemId,
                                   @RequestHeader("X-Sharer-User-Id") Long userId) throws ItemNotFoundException {
        return itemService.getItemById(itemId);
    }

    @GetMapping
    public List<ManyItemsResponseDto> getAllUserOwnItems(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.getAllItemsInUserOwn(userId);
    }

    @GetMapping("/search")
    public List<ManyItemsResponseDto> getAllFindingItems(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                         @RequestParam String text) {
        return itemService.getItemsBySearch(text);
    }


}
