package com.list.manager.repository;

import com.list.manager.entities.ListEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EntryRepository extends JpaRepository <ListEntry, Long> {
}