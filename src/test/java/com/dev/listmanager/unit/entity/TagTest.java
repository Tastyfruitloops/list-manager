package com.dev.listmanager.unit.entity;

import com.dev.listmanager.entity.Item;
import com.dev.listmanager.entity.ItemList;
import com.dev.listmanager.entity.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TagTest {
    private Tag tag;
    private String name;
    private ItemList taggedList;
    private List<Item> items;

    @BeforeEach
    public void setUp() {
        name = "Test Tag";
        taggedList = new ItemList();
        items = new ArrayList<>();
        items.add(new Item());

        tag = new Tag(name, taggedList);
        tag.setItems(items);
    }

    @Test
    public void testConstructor() {
        assertEquals(name, tag.getName());
        assertEquals(taggedList, tag.getList());
        assertEquals(items, tag.getItems());
    }

    @Test
    public void testName() {
        String name = "Test Tag";
        tag.setName(name);
        assertEquals(name, tag.getName());
    }

    @Test
    public void testItems() {
        List<Item> items = new ArrayList<>();
        items.add(new Item());
        tag.setItems(items);
        assertEquals(items, tag.getItems());
    }
}