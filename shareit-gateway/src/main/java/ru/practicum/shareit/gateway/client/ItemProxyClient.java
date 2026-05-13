package ru.practicum.shareit.gateway.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.practicum.shareit.comment.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;

import java.util.Map;

@Component
public class ItemProxyClient extends BaseClient {

    public ItemProxyClient(RestTemplate rest, @Value("${shareit.server.url}") String serverUrl) {
        super(rest, serverUrl);
    }

    public ResponseEntity<String> create(ItemCreateDto dto, long userId) {
        return httpPost("/items", dto, userId);
    }

    public ResponseEntity<String> update(long itemId, ItemUpdateDto dto, long userId) {
        return httpPatch("/items/" + itemId, dto, userId);
    }

    public ResponseEntity<String> getById(long itemId, long userId) {
        return httpGet("/items/" + itemId, userId);
    }

    public ResponseEntity<String> getAll(long userId) {
        return httpGet("/items", userId);
    }

    public ResponseEntity<String> search(String text, long userId) {
        return httpGet("/items/search", userId, Map.of("text", text));
    }

    public ResponseEntity<String> addComment(long itemId, CommentCreateDto dto, long userId) {
        return httpPost("/items/" + itemId + "/comment", dto, userId);
    }
}
