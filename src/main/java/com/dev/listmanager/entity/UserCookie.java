package com.dev.listmanager.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

@Entity
@Table(name = "user_cookies")
public class UserCookie {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank(message = "Cookie cannot be blank")
    @Column(name = "cookie")
    private String cookie;

    public UserCookie() {
    }

    public UserCookie(String cookie) {
        this.cookie = cookie;
    }

    public String getCookie() {
        return cookie;
    }
}
