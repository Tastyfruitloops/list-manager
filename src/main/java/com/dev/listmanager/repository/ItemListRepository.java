package com.dev.listmanager.repository;

import com.dev.listmanager.entity.ItemList;
import com.dev.listmanager.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ItemListRepository extends JpaRepository<ItemList, UUID> {
    Optional<ItemList> findById(UUID id);

    Optional<ItemList> findByName(String name);

    List<ItemList> findAllByOwner(User user);
}
