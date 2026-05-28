package ru.practicum.shareit.item.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query("SELECT i FROM Item i WHERE i.available = true AND " +
            "(LOWER(i.name) LIKE LOWER(CONCAT('%', :text, '%')) OR " +
            "LOWER(i.description) LIKE LOWER(CONCAT('%', :text, '%')))")
    List<Item> searchByText(@Param("text") String text);

    List<Item> findByOwnerId(long ownerId);

    boolean existsByIdAndOwnerId(long itemId, long ownerId);

    List<Item> findByOwnerIdAndAvailableTrue(long ownerId);

    List<Item> findByRequestIdOrderByIdAsc(long requestId);

    List<Item> findByRequestIdInOrderByIdAsc(List<Long> requestIds);

}
