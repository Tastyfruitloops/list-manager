package com.dev.listmanager.repository;

import com.dev.listmanager.entity.UserCookie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserCookieRepository extends JpaRepository<UserCookie, UUID> {
    Optional<UserCookie> findByCookie(String cookie);
}
