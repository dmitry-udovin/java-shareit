package ru.practicum.shareit.item.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "items")
public class Item {

    @Id
    private long id;

    private String name;
    private String description;
    @Column(name = "owner_id")
    private Long ownerId;
    @Column(name = "count_rents")
    private int countRents;
    private Boolean available;

}