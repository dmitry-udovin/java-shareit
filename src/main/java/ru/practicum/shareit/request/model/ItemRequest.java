package ru.practicum.shareit.request.model;

import lombok.Data;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class ItemRequest {

    private long id;

    private String description;
    private Long requestorId;
    private RequestStatus status = RequestStatus.WAITING;
    private LocalDateTime created;
    private List<Item> items = new ArrayList<>();

}
