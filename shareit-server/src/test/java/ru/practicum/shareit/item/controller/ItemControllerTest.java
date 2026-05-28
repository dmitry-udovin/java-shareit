package ru.practicum.shareit.item.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.comment.dto.CommentCreateDto;
import ru.practicum.shareit.comment.dto.CommentResponseDto;
import ru.practicum.shareit.handler.ErrorHandler;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.service.ItemService;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
@Import(ErrorHandler.class)
class ItemControllerTest {

    private static final String USER_HEADER = "X-Sharer-User-Id";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ItemService itemService;

    @Test
    void createItem_returnsDto() throws Exception {
        when(itemService.saveItem(any(ItemCreateDto.class), eq(1L)))
                .thenReturn(new ItemResponseDto(10L, "Hammer", "Heavy", true));

        mockMvc.perform(post("/items")
                        .header(USER_HEADER, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new ItemCreateDto("Hammer", "Heavy", true, null))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.name").value("Hammer"));
    }

    @Test
    void getItemById_returnsDto() throws Exception {
        when(itemService.getItemById(7L)).thenReturn(new ItemResponseDto(7L, "X", "D", true));

        mockMvc.perform(get("/items/7").header(USER_HEADER, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("X"));
    }

    @Test
    void getItemById_notFound() throws Exception {
        when(itemService.getItemById(99L)).thenThrow(new ItemNotFoundException("no"));

        mockMvc.perform(get("/items/99").header(USER_HEADER, 1))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllUserOwnItems_returnsList() throws Exception {
        when(itemService.getAllItemsInUserOwn(2L)).thenReturn(List.of(new ItemResponseDto(1L, "A", "B", true)));

        mockMvc.perform(get("/items").header(USER_HEADER, 2))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void search_returnsList() throws Exception {
        when(itemService.getItemsBySearch("text")).thenReturn(List.of());

        mockMvc.perform(get("/items/search").header(USER_HEADER, 1).param("text", "text"))
                .andExpect(status().isOk());
    }

    @Test
    void updateItem_returnsDto() throws Exception {
        when(itemService.updateItem(any(ItemUpdateDto.class), eq(1L), eq(5L)))
                .thenReturn(new ItemResponseDto(5L, "N", "D", false));

        mockMvc.perform(patch("/items/5")
                        .header(USER_HEADER, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new ItemUpdateDto("N", "D", false))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.available").value(false));
    }

    @Test
    void addComment_returnsDto() throws Exception {
        LocalDateTime created = LocalDateTime.of(2026, 1, 1, 12, 0);
        when(itemService.addComment(eq(3L), eq(1L), any(CommentCreateDto.class)))
                .thenReturn(new CommentResponseDto(1L, "ok", "Ann", created));

        mockMvc.perform(post("/items/3/comment")
                        .header(USER_HEADER, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new CommentCreateDto("ok"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value("ok"));
    }
}
