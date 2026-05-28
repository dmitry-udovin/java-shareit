package ru.practicum.shareit.request.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.handler.ErrorHandler;
import ru.practicum.shareit.item.dto.ItemShortDto;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemRequestController.class)
@Import(ErrorHandler.class)
class ItemRequestControllerTest {

    private static final String USER_HEADER = "X-Sharer-User-Id";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ItemRequestService itemRequestService;

    @Test
    void create_returnsDto() throws Exception {
        LocalDateTime created = LocalDateTime.of(2026, 3, 1, 12, 0);
        ItemRequestResponseDto dto = new ItemRequestResponseDto(
                1L, "Need drill", 2L, created, List.of());
        when(itemRequestService.create(eq(2L), any(ItemRequestCreateDto.class))).thenReturn(dto);

        mockMvc.perform(post("/requests")
                        .header(USER_HEADER, 2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new ItemRequestCreateDto("Need drill"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.description").value("Need drill"));
    }

    @Test
    void getOwnerRequests_returnsList() throws Exception {
        LocalDateTime created = LocalDateTime.of(2026, 3, 2, 12, 0);
        when(itemRequestService.getOwnerRequests(1L)).thenReturn(List.of(
                new ItemRequestResponseDto(1L, "A", 1L, created, List.of())));

        mockMvc.perform(get("/requests").header(USER_HEADER, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void getAllRequests_returnsPage() throws Exception {
        LocalDateTime created = LocalDateTime.of(2026, 3, 3, 12, 0);
        when(itemRequestService.getAllRequests(1L, 0, 10)).thenReturn(List.of(
                new ItemRequestResponseDto(2L, "B", 3L, created,
                        List.of(new ItemShortDto(5L, "Item", 4L)))));

        mockMvc.perform(get("/requests/all").header(USER_HEADER, 1).param("from", "0").param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].items[0].name").value("Item"));
    }

    @Test
    void getById_returnsDto() throws Exception {
        LocalDateTime created = LocalDateTime.of(2026, 3, 4, 12, 0);
        ItemRequestResponseDto dto = new ItemRequestResponseDto(
                9L, "Desc", 1L, created, List.of());
        when(itemRequestService.getRequestById(9L, 1L)).thenReturn(dto);

        mockMvc.perform(get("/requests/9").header(USER_HEADER, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(9));
    }
}
