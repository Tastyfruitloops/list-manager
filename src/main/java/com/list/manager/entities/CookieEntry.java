package com.list.manager.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "cookies")
public class CookieEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "authId")
    private Long authId;
    @Column(name = "cookie")
    private String cookie;

    public CookieEntry() {
    }

    public CookieEntry(Long authId, String cookie) {
        this.authId = authId;
        this.cookie = cookie;
    }

    public Long getAuthId() {
        return authId;
    }

    public String getCookie() {
        return cookie;
    }
}