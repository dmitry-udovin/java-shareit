package ru.practicum.shareit.gateway.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

@Component
public class UserProxyClient extends BaseClient {

    public UserProxyClient(RestTemplate rest, @Value("${shareit.server.url}") String serverUrl) {
        super(rest, serverUrl);
    }

    public ResponseEntity<String> create(UserCreateDto dto) {
        return httpPost("/users", dto, null);
    }

    public ResponseEntity<String> update(long userId, UserUpdateDto dto) {
        return httpPatch("/users/" + userId, dto, null);
    }

    public ResponseEntity<String> findAll() {
        return httpGet("/users", null);
    }

    public ResponseEntity<String> findById(long userId) {
        return httpGet("/users/" + userId, null);
    }

    public ResponseEntity<String> delete(long userId) {
        return httpDelete("/users/" + userId, null);
    }
}
