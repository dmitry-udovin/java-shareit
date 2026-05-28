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

    protected HttpHeaders jsonHeaders() {
        HttpHeaders h = new HttpHeaders();
        h.setContentType(MediaType.APPLICATION_JSON);
        return h;
    }

    protected HttpHeaders jsonHeaders(long sharerUserId) {
        HttpHeaders h = jsonHeaders();
        h.add("X-Sharer-User-Id", Long.toString(sharerUserId));
        return h;
    }

    protected ResponseEntity<String> httpGet(String path) {
        return rest.exchange(URI.create(base() + path), HttpMethod.GET, new HttpEntity<>(jsonHeaders()), String.class);
    }

    protected ResponseEntity<String> httpGet(String path, long userId) {
        return rest.exchange(URI.create(base() + path), HttpMethod.GET, new HttpEntity<>(jsonHeaders(userId)), String.class);
    }

    protected ResponseEntity<String> httpGet(String path, long userId, Map<String, String> query) {
        UriComponentsBuilder b = UriComponentsBuilder.fromUriString(base() + path);
        if (query != null) {
            query.forEach(b::queryParam);
        }
        return rest.exchange(b.build().encode().toUri(), HttpMethod.GET, new HttpEntity<>(jsonHeaders(userId)), String.class);
    }

    protected ResponseEntity<String> httpPost(String path, Object body) {
        return rest.exchange(URI.create(base() + path), HttpMethod.POST, new HttpEntity<>(body, jsonHeaders()), String.class);
    }

    protected ResponseEntity<String> httpPost(String path, Object body, long userId) {
        return rest.exchange(URI.create(base() + path), HttpMethod.POST, new HttpEntity<>(body, jsonHeaders(userId)), String.class);
    }

    protected ResponseEntity<String> httpPatch(String path, Object body) {
        return rest.exchange(URI.create(base() + path), HttpMethod.PATCH, new HttpEntity<>(body, jsonHeaders()), String.class);
    }

    protected ResponseEntity<String> httpPatch(String path, Object body, long userId) {
        return rest.exchange(URI.create(base() + path), HttpMethod.PATCH, new HttpEntity<>(body, jsonHeaders(userId)), String.class);
    }

    protected ResponseEntity<String> httpPatchUri(URI uri, long userId) {
        return rest.exchange(uri, HttpMethod.PATCH, new HttpEntity<>(jsonHeaders(userId)), String.class);
    }

    protected ResponseEntity<String> httpDelete(String path) {
        return rest.exchange(URI.create(base() + path), HttpMethod.DELETE, new HttpEntity<>(jsonHeaders()), String.class);
    }

    protected ResponseEntity<String> httpDelete(String path, long userId) {
        return rest.exchange(URI.create(base() + path), HttpMethod.DELETE, new HttpEntity<>(jsonHeaders(userId)), String.class);
    }

    public static ResponseEntity<String> forward(ResponseEntity<String> fromServer) {
        MediaType ct = fromServer.getHeaders().getContentType();
        return ResponseEntity.status(fromServer.getStatusCode())
                .contentType(ct != null ? ct : MediaType.APPLICATION_JSON)
                .body(fromServer.getBody());
    }
}
