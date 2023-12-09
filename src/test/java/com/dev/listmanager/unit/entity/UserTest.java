package com.dev.listmanager.unit.entity;

import com.dev.listmanager.entity.ItemList;
import com.dev.listmanager.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {
    private User user;
    private String username;
    private String password;

    @BeforeEach
    public void setUp() {
        username = "testUser";
        password = "testPassword";
        user = new User(username, password);
    }

    @Test
    public void testConstructor() {
        assertNotNull(user);
        assertEquals(username, user.getUsername());
        assertNotNull(user.getHashedPassword());
    }


    @Test
    public void testGetUsername() {
        assertEquals(username, user.getUsername());
    }

    @Test
    public void testGetHashedPassword() {
        assertNotNull(user.getHashedPassword());
    }

    @Test
    public void testGetLists() {
        assertNotNull(user.getLists());
    }

    @Test
    public void testGetPublicLists() {
        List<ItemList> lists = new ArrayList<>();
        ItemList publicList = new ItemList();
        publicList.setPublic(true);
        lists.add(publicList);
        user.setLists(lists);

        assertEquals(1, user.getPublicLists().size());
    }

    @Test
    public void testAddList() {
        ItemList list = new ItemList();
        user.addList(list);

        assertEquals(1, user.getLists().size());
    }

    @Test
    public void testSetPassword() {
        String newPassword = "newPassword";
        user.setPassword(newPassword);

        assertNotEquals(password, user.getHashedPassword());
    }
}