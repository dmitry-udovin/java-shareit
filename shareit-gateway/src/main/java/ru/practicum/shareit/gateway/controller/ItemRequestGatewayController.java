package ru.practicum.shareit.gateway.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.gateway.client.BaseClient;
import ru.practicum.shareit.gateway.client.ItemRequestProxyClient;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;

@RestController
@RequestMapping("/requests")
@RequiredArgsConstructor
@Validated
public class ItemRequestGatewayController {

    private final ItemRequestProxyClient itemRequestProxyClient;

    @PostMapping
    public ResponseEntity<String> create(@RequestHeader("X-Sharer-User-Id") Long userId,
                                         @Valid @RequestBody ItemRequestCreateDto dto) {
        return BaseClient.forward(itemRequestProxyClient.create(dto, userId));
    }

    @GetMapping
    public ResponseEntity<String> getOwn(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return BaseClient.forward(itemRequestProxyClient.getOwn(userId));
    }

    @GetMapping("/all")
    public ResponseEntity<String> getAll(@RequestHeader("X-Sharer-User-Id") Long userId,
                                         @RequestParam(defaultValue = "0") Integer from,
                                         @RequestParam(defaultValue = "10") Integer size) {
        return BaseClient.forward(itemRequestProxyClient.getAllOthers(from, size, userId));
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<String> getById(@PathVariable Long requestId,
                                          @RequestHeader("X-Sharer-User-Id") Long userId) {
        return BaseClient.forward(itemRequestProxyClient.getById(requestId, userId));
    }
}
