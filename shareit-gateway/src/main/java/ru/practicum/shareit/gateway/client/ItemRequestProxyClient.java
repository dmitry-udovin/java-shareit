package ru.practicum.shareit.gateway.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;

import java.util.HashMap;
import java.util.Map;

@Component
public class ItemRequestProxyClient extends BaseClient {

    public ItemRequestProxyClient(RestTemplate rest, @Value("${shareit.server.url}") String serverUrl) {
        super(rest, serverUrl);
    }

    public ResponseEntity<String> create(ItemRequestCreateDto dto, long userId) {
        return httpPost("/requests", dto, userId);
    }

    public ResponseEntity<String> getOwn(long userId) {
        return httpGet("/requests", userId);
    }

    public ResponseEntity<String> getAllOthers(int from, int size, long userId) {
        Map<String, String> q = new HashMap<>();
        q.put("from", String.valueOf(from));
        q.put("size", String.valueOf(size));
        return httpGet("/requests/all", userId, q);
    }

    public ResponseEntity<String> getById(long requestId, long userId) {
        return httpGet("/requests/" + requestId, userId);
    }
}
