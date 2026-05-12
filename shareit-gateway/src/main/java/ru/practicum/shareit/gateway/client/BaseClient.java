package ru.practicum.shareit.gateway.client;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Map;

@RequiredArgsConstructor
public abstract class BaseClient {

    protected final RestTemplate rest;
    protected final String serverUrl;

    protected String base() {
        return serverUrl.endsWith("/") ? serverUrl.substring(0, serverUrl.length() - 1) : serverUrl;
    }

    protected HttpHeaders headers(Long userId) {
        HttpHeaders h = new HttpHeaders();
        h.setContentType(MediaType.APPLICATION_JSON);
        if (userId != null && userId != 0L) {
            h.add("X-Sharer-User-Id", Long.toString(userId));
        }
        return h;
    }

    protected ResponseEntity<String> httpGet(String path, Long userId) {
        return rest.exchange(URI.create(base() + path), HttpMethod.GET, new HttpEntity<>(headers(userId)), String.class);
    }

    protected ResponseEntity<String> httpGet(String path, Long userId, Map<String, String> query) {
        UriComponentsBuilder b = UriComponentsBuilder.fromUriString(base() + path);
        if (query != null) {
            query.forEach(b::queryParam);
        }
        return rest.exchange(b.build().encode().toUri(), HttpMethod.GET, new HttpEntity<>(headers(userId)), String.class);
    }

    protected ResponseEntity<String> httpPost(String path, Object body, Long userId) {
        return rest.exchange(URI.create(base() + path), HttpMethod.POST, new HttpEntity<>(body, headers(userId)), String.class);
    }

    protected ResponseEntity<String> httpPatch(String path, Object body, Long userId) {
        return rest.exchange(URI.create(base() + path), HttpMethod.PATCH, new HttpEntity<>(body, headers(userId)), String.class);
    }

    protected ResponseEntity<String> httpPatchUri(URI uri, Long userId) {
        return rest.exchange(uri, HttpMethod.PATCH, new HttpEntity<>(headers(userId)), String.class);
    }

    protected ResponseEntity<String> httpDelete(String path, Long userId) {
        return rest.exchange(URI.create(base() + path), HttpMethod.DELETE, new HttpEntity<>(headers(userId)), String.class);
    }

    protected static ResponseEntity<String> forward(ResponseEntity<String> fromServer) {
        MediaType ct = fromServer.getHeaders().getContentType();
        return ResponseEntity.status(fromServer.getStatusCode())
                .contentType(ct != null ? ct : MediaType.APPLICATION_JSON)
                .body(fromServer.getBody());
    }
}
