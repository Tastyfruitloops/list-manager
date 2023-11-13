package com.list.manager.repository;


import com.list.manager.entities.CookieEntry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CookieRepository extends JpaRepository <CookieEntry, Long> {
}

