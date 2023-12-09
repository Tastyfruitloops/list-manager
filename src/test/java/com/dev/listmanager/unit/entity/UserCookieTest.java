package com.dev.listmanager.unit.entity;

import com.dev.listmanager.entity.UserCookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UserCookieTest {
    private final String cookie = "testCookie";
    private UserCookie userCookie;

    @BeforeEach
    public void setUp() {
        userCookie = new UserCookie(cookie);
    }

    @Test
    public void testConstructor() {
        assertNotNull(userCookie);
        assertEquals(cookie, userCookie.getCookie());
    }

    @Test
    public void testGetCookie() {
        assertEquals(cookie, userCookie.getCookie());
    }
}