package com.dev.listmanager.entity;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "user_cookies")
public class UserCookie {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "cookie")
    private String cookie;

    public UserCookie() {
    }

    public UserCookie(String cookie) {
        this.cookie = cookie;
    }

    public UUID getId() {
        return id;
    }

    public String getCookie() {
        return cookie;
    }
}
