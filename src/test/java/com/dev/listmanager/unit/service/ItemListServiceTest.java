package com.dev.listmanager.unit.service;

import com.dev.listmanager.dto.ItemDto;
import com.dev.listmanager.dto.ItemListDto;
import com.dev.listmanager.dto.TagDto;
import com.dev.listmanager.entity.Item;
import com.dev.listmanager.entity.ItemList;
import com.dev.listmanager.entity.Tag;
import com.dev.listmanager.entity.User;
import com.dev.listmanager.exception.ListArchivedException;
import com.dev.listmanager.exception.NotFoundException;
import com.dev.listmanager.exception.UnathorizedException;
import com.dev.listmanager.repository.ItemListRepository;
import com.dev.listmanager.repository.ItemRepository;
import com.dev.listmanager.repository.TagRepository;
import com.dev.listmanager.repository.UserRepository;
import com.dev.listmanager.service.ItemListService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ItemListServiceTest {
    @Mock
    private ItemListRepository itemListRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private TagRepository tagRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ItemListService itemListService;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddItem() throws ListArchivedException {
        UUID uuid = UUID.randomUUID();
        User user = new User("testUser", "testPassword");
        ItemList itemList = new ItemList("testList", user);
        ItemDto itemDto = new ItemDto("testItem", Collections.singletonList(uuid.toString()));
        Item item = new Item(itemList, "testItem", Collections.emptyList());
        when(itemListRepository.findById(uuid)).thenReturn(Optional.of(itemList));
        when(tagRepository.findById(uuid)).thenReturn(Optional.of(new Tag("testTag", itemList)));
        when(itemListRepository.save(any(ItemList.class))).thenReturn(itemList);
        when(itemRepository.save(any(Item.class))).thenReturn(item);

        Item actualItem = itemListService.addItem(uuid.toString(), itemDto);

        assertEquals(item, actualItem);
    }

    @Test
    public void testDeleteItem() throws ListArchivedException, NotFoundException {
        UUID uuid = UUID.randomUUID();
        User user = new User("testUser", "testPassword");
        ItemList itemList = new ItemList("testList", user);
        Item item = new Item(itemList, "testItem", Collections.emptyList());
        when(itemRepository.findById(uuid)).thenReturn(Optional.of(item));

        itemListService.deleteItem(uuid.toString());

        verify(itemRepository, times(1)).delete(item);
    }

    @Test
    public void testUpdateItem() throws ListArchivedException, NotFoundException {
        UUID uuid = UUID.randomUUID();
        User user = new User("testUser", "testPassword");
        ItemList itemList = new ItemList("testList", user);
        Item item = new Item(itemList, "testItem", Collections.emptyList());
        Item expectedItem = new Item(itemList, "updatedItem", Collections.emptyList());

        when(itemRepository.findById(uuid)).thenReturn(Optional.of(item));
        when(itemRepository.save(any(Item.class))).thenReturn(expectedItem);

        Item actualItem = itemListService.updateItem(uuid.toString(), "{\"name\":\"updatedItem\"}");

        assertEquals(expectedItem, actualItem);
    }

    @Test
    public void testAddTag() throws ListArchivedException, NotFoundException {
        UUID uuid = UUID.randomUUID();
        User user = new User("testUser", "testPassword");
        ItemList itemList = new ItemList("testList", user);
        TagDto tagDto = new TagDto("testTag");
        Tag expectedTag = new Tag(tagDto.getName(), itemList);
        when(itemListRepository.findById(uuid)).thenReturn(Optional.of(itemList));
        when(tagRepository.save(any(Tag.class))).thenReturn(expectedTag);

        Tag actualTag = itemListService.addTag(uuid.toString(), tagDto.getName());

        verify(tagRepository, times(1)).save(any(Tag.class));
        assertEquals(expectedTag.getName(), actualTag.getName());
    }

    @Test
    public void testDeleteTag() throws ListArchivedException, NotFoundException {
        UUID uuid = UUID.randomUUID();
        User user = new User("testUser", "testPassword");
        ItemList itemList = new ItemList("testList", user);
        TagDto tagDto = new TagDto("testTag");

        when(tagRepository.findById(uuid)).thenReturn(Optional.of(new Tag(tagDto.getName(), itemList)));

        itemListService.deleteTag(uuid.toString());

        verify(tagRepository, times(1)).delete(any(Tag.class));
    }

    @Test
    public void testUpdateTag() throws ListArchivedException, NotFoundException {
        UUID uuid = UUID.randomUUID();
        User user = new User("testUser", "testPassword");
        ItemList itemList = new ItemList("testList", user);
        Tag expectedTag = new Tag("updatedTag", itemList);
        when(tagRepository.findById(uuid)).thenReturn(Optional.of(new Tag("testTag", itemList)));
        when(tagRepository.save(any(Tag.class))).thenReturn(expectedTag);

        Tag actualTag = itemListService.updateTag(uuid.toString(), "updatedTag");

        verify(tagRepository, times(1)).save(any(Tag.class));
        assertEquals(expectedTag.getName(), actualTag.getName());
    }

    @Test
    public void testGetAllLists() {
        itemListService.getAllLists();

        verify(itemListRepository, times(1)).findAll();
    }

    @Test
    public void testGetListById() throws NotFoundException {
        UUID uuid = UUID.randomUUID();
        User user = new User("testUser", "testPassword");
        ItemList itemList = new ItemList("testList", user);
        when(itemListRepository.findById(uuid)).thenReturn(Optional.of(itemList));

        ItemList actualList = itemListService.getListById(uuid.toString());

        assertEquals(itemList, actualList);
    }

    @Test
    public void testGetTagById() throws NotFoundException {
        UUID uuid = UUID.randomUUID();
        User user = new User("testUser", "testPassword");
        ItemList itemList = new ItemList("testList", user);
        Tag tag = new Tag("testTag", itemList);
        when(tagRepository.findById(uuid)).thenReturn(Optional.of(tag));

        Tag actualTag = itemListService.getTagById(uuid.toString());

        assertEquals(tag, actualTag);
    }

    @Test
    public void testGetListByName() throws NotFoundException {
        String name = "testList";
        User user = new User("testUser", "testPassword");
        ItemList itemList = new ItemList("testList", user);
        when(itemListRepository.findByName(name)).thenReturn(Optional.of(itemList));

        ItemList actualList = itemListService.getListByName(name);

        assertEquals(itemList, actualList);
    }

    @Test
    public void testGetItemByName() throws NotFoundException {
        String name = "testItem";
        User user = new User("testUser", "testPassword");
        ItemList itemList = new ItemList("testList", user);
        Item item = new Item(itemList, "testItem", Collections.emptyList());
        when(itemRepository.findByName(name)).thenReturn(Optional.of(item));

        Item actualItem = itemListService.getItemByName(name);

        assertEquals(item, actualItem);
    }

    @Test
    public void testGetItemById() throws NotFoundException {
        UUID uuid = UUID.randomUUID();
        User user = new User("testUser", "testPassword");
        ItemList itemList = new ItemList("testList", user);
        Item item = new Item(itemList, "testItem", Collections.emptyList());
        when(itemRepository.findById(uuid)).thenReturn(Optional.of(item));

        Item actualItem = itemListService.getItemById(uuid.toString());

        assertEquals(item, actualItem);
    }

    @Test
    public void testGetListsByUserId() {
        UUID uuid = UUID.randomUUID();
        User user = new User("testUser", "testPassword");
        when(userRepository.findById(uuid)).thenReturn(Optional.of(user));

        itemListService.getListsByUserId(uuid.toString());

        verify(itemListRepository, times(1)).findAllByOwner(user);
    }

    @Test
    public void testGetListsByCookie() throws UnathorizedException, NotFoundException {
        String cookie = "testUser&testCookie";
        User user = new User("testUser", "testPassword");
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));

        itemListService.getListsByCookie(cookie);

        verify(itemListRepository, times(1)).findAllByOwner(user);
    }

    @Test
    public void testCreateList() throws UnathorizedException, NotFoundException {
        String cookie = "testUser&testCookie";
        ItemListDto itemListDto = new ItemListDto("testList");
        User user = new User("testUser", "testPassword");
        ItemList expectedList = new ItemList(itemListDto.getName(), user);

        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
        when(itemListRepository.save(any(ItemList.class))).thenReturn(expectedList);

        ItemList actualList = itemListService.createList(cookie, itemListDto);

        verify(itemListRepository, times(1)).save(any(ItemList.class));
        assertEquals(expectedList.getName(), actualList.getName());
    }

    @Test
    public void testUpdateList() throws NotFoundException {
        UUID uuid = UUID.randomUUID();
        User user = new User("testUser", "testPassword");
        String attributes = "{\"name\":\"updatedList\"}";
        ItemList itemList = new ItemList("testList", user);
        ItemList expectedItemList = new ItemList("updatedList", user);
        when(itemListRepository.findById(uuid)).thenReturn(Optional.of(itemList));
        when(itemListRepository.save(any(ItemList.class))).thenReturn(expectedItemList);

        ItemList actualList = itemListService.updateList(uuid.toString(), attributes);

        verify(itemListRepository, times(1)).save(any(ItemList.class));
        assertEquals(expectedItemList.getName(), actualList.getName());

    }

    @Test
    public void testDeleteList() throws NotFoundException {
        UUID uuid = UUID.randomUUID();
        User user = new User("testUser", "testPassword");
        ItemList itemList = new ItemList("testList", user);
        when(itemListRepository.findById(uuid)).thenReturn(Optional.of(itemList));

        itemListService.deleteList(uuid.toString());

        verify(itemListRepository, times(1)).delete(any(ItemList.class));
    }

    @Test
    public void testListArchivedException() {
        UUID uuid = UUID.randomUUID();
        User user = new User("testUser", "testPassword");
        ItemList itemList = new ItemList("testList", user);
        itemList.setArchived(true);
        ItemDto itemDto = new ItemDto("testItem", Collections.singletonList(uuid.toString()));
        Item item = new Item(itemList, "testItem", Collections.emptyList());
        TagDto tagDto = new TagDto("testTag");

        when(itemListRepository.findById(uuid)).thenReturn(Optional.of(itemList));
        when(itemRepository.findById(uuid)).thenReturn(Optional.of(item));
        when(tagRepository.findById(uuid)).thenReturn(Optional.of(new Tag(tagDto.getName(), itemList)));

        assertThrows(ListArchivedException.class, () -> itemListService.addItem(uuid.toString(), itemDto));
        assertThrows(ListArchivedException.class, () -> itemListService.deleteItem(uuid.toString()));
        assertThrows(ListArchivedException.class, () -> itemListService.updateItem(uuid.toString(), "{\"name\":\"updatedItem\"}"));
        assertThrows(ListArchivedException.class, () -> itemListService.addTag(uuid.toString(), tagDto.getName()));
        assertThrows(ListArchivedException.class, () -> itemListService.deleteTag(uuid.toString()));
        assertThrows(ListArchivedException.class, () -> itemListService.updateTag(uuid.toString(), "updatedTag"));
    }
}