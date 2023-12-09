package com.dev.listmanager.unit.entity;

import com.dev.listmanager.entity.Item;
import com.dev.listmanager.entity.ItemList;
import com.dev.listmanager.entity.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ItemTest {
    private Item item;
    private String name;
    private ItemList hostList;
    private List<Tag> tags;

    @BeforeEach
    public void setUp() {
        name = "Test Item";
        hostList = new ItemList();
        tags = new ArrayList<>();
        tags.add(new Tag());

        item = new Item(hostList, name, tags);
    }

    @Test
    public void testConstructor() {
        assertEquals(name, item.getName());
        assertEquals(hostList, item.getList());
        assertEquals(tags, item.getTags());
    }

    @Test
    public void testName() {
        String name = "Test Item";
        item.setName(name);
        assertEquals(name, item.getName());
    }

    @Test
    public void testHostList() {
        ItemList hostList = new ItemList();
        item.setList(hostList);
        assertEquals(hostList, item.getList());
    }

    @Test
    public void testTags() {
        List<Tag> tags = new ArrayList<>();
        tags.add(new Tag());
        item.setTags(tags);
        assertNotNull(item.getTags());
        assertEquals(tags, item.getTags());
    }
}