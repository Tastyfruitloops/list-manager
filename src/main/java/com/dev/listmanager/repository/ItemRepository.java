package com.dev.listmanager.repository;

import com.dev.listmanager.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ItemRepository extends JpaRepository<Item, UUID> {
    Optional<Item> findById(UUID id);

    Optional<Item> findByName(String name);

    Optional<Item> findByNameLikeIgnoreCase(String name);
}
