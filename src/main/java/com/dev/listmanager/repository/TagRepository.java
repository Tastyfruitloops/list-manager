package com.dev.listmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.dev.listmanager.entity.Tag;

import java.util.UUID;

public interface TagRepository extends JpaRepository<Tag, UUID> {
}
