package ru.practicum.shareit.gateway.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.comment.dto.CommentCreateDto;
import ru.practicum.shareit.gateway.client.BaseClient;
import ru.practicum.shareit.gateway.client.ItemProxyClient;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Validated
public class ItemGatewayController {

    private final ItemProxyClient itemProxyClient;

    @PostMapping
    public ResponseEntity<String> create(@Valid @RequestBody ItemCreateDto dto,
                                         @RequestHeader("X-Sharer-User-Id") long userId) {
        return BaseClient.forward(itemProxyClient.create(dto, userId));
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<String> update(@PathVariable long itemId,
                                         @Valid @RequestBody ItemUpdateDto dto,
                                         @RequestHeader("X-Sharer-User-Id") long userId) {
        return BaseClient.forward(itemProxyClient.update(itemId, dto, userId));
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<String> getById(@PathVariable long itemId,
                                          @RequestHeader("X-Sharer-User-Id") long userId) {
        return BaseClient.forward(itemProxyClient.getById(itemId, userId));
    }

    @GetMapping
    public ResponseEntity<String> getAll(@RequestHeader("X-Sharer-User-Id") long userId) {
        return BaseClient.forward(itemProxyClient.getAll(userId));
    }

    @GetMapping("/search")
    public ResponseEntity<String> search(@RequestHeader("X-Sharer-User-Id") long userId,
                                         @RequestParam String text) {
        return BaseClient.forward(itemProxyClient.search(text, userId));
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<String> addComment(@PathVariable long itemId,
                                             @Valid @RequestBody CommentCreateDto dto,
                                             @RequestHeader("X-Sharer-User-Id") long userId) {
        return BaseClient.forward(itemProxyClient.addComment(itemId, dto, userId));
    }
}
