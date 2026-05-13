package ru.practicum.shareit.request.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {

    List<ItemRequest> findByRequesterIdOrderByCreatedDesc(long requesterId);

    List<ItemRequest> findByRequesterIdNotOrderByCreatedDesc(long requesterId, Pageable pageable);

    boolean existsByIdAndRequesterId(long requestId, long requesterId);

}
