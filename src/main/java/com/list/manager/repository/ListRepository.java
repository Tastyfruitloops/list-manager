package com.list.manager.repository;

import com.list.manager.entities.ItemList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ListRepository extends JpaRepository<ItemList, Long> {
}
