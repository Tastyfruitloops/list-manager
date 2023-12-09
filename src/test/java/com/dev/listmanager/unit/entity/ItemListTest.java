package com.dev.listmanager.unit.entity;

import com.dev.listmanager.entity.Item;
import com.dev.listmanager.entity.ItemList;
import com.dev.listmanager.entity.Tag;
import com.dev.listmanager.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ItemListTest {
    private ItemList itemList;
    private String name;
    private User owner;
    private List<Tag> tags;

    @BeforeEach
    public void setUp() {
        name = "Test List";
        owner = new User();
        tags = new ArrayList<>();
        itemList = new ItemList(name, owner);
    }

    @Test
    public void testConstructor() {
        assertEquals(name, itemList.getName());
        assertEquals(owner, itemList.getOwner());
        assertFalse(itemList.isPublic());
        assertFalse(itemList.isArchived());
    }

    @Test
    public void testName() {
        String name = "Test List";
        itemList.setName(name);
        assertEquals(name, itemList.getName());
    }

    @Test
    public void testItems() {
        List<Item> items = new ArrayList<>();
        items.add(new Item());
        itemList.setItems(items);
        assertEquals(items, itemList.getItems());
    }

    @Test
    public void testAddTag() {
        Tag tag = new Tag();
        itemList.addTag(tag);
        tags.add(tag);
        assertEquals(tags, itemList.getTags());
    }

    @Test
    public void testRemoveTag() {
        Tag tag = new Tag();
        itemList.addTag(tag);
        itemList.removeTag(tag);
        assertEquals(0, itemList.getTags().size());
    }

    @Test
    public void testAddItem() {
        Item item = new Item();
        itemList.addItem(item);
        assertEquals(1, itemList.getItems().size());
        assertEquals(item, itemList.getItems().get(0));
    }

    @Test
    public void testRemoveItem() {
        Item item = new Item();
        itemList.addItem(item);
        itemList.removeItem(item);
        assertEquals(0, itemList.getItems().size());
    }

    @Test
    public void testIsPublic() {
        itemList.setPublic(true);
        assertTrue(itemList.isPublic());
    }

    @Test
    public void testIsArchived() {
        itemList.setArchived(true);
        assertTrue(itemList.isArchived());
    }
}