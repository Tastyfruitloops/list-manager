package com.dev.listmanager.service.interfaces;

import com.dev.listmanager.dto.ItemDto;
import com.dev.listmanager.dto.ItemListDto;
import com.dev.listmanager.entity.Item;
import com.dev.listmanager.entity.ItemList;
import com.dev.listmanager.entity.Tag;
import com.dev.listmanager.exception.ListArchivedException;
import com.dev.listmanager.exception.NotFoundException;
import com.dev.listmanager.exception.UnathorizedException;

import java.util.List;

public interface IItemListService {
    Item addItem(String uuid, ItemDto itemDto) throws ListArchivedException;

    void deleteItem(String uuid) throws NotFoundException, ListArchivedException;

    Item updateItem(String uuid, String attributes) throws ListArchivedException, NotFoundException;

    Tag addTag(String uuid, String name) throws NotFoundException, ListArchivedException;

    void deleteTag(String uuid) throws NotFoundException, ListArchivedException;

    Tag updateTag(String uuid, String name) throws NotFoundException, ListArchivedException;

    List<ItemList> getAllLists();

    ItemList getListById(String id) throws NotFoundException;

    Tag getTagById(String id) throws IllegalArgumentException, NotFoundException;

    ItemList getListByName(String name) throws NotFoundException;

    Item getItemByName(String name) throws NotFoundException;

    Item getItemById(String id) throws NotFoundException;

    List<ItemList> getListsByUserId(String userId);

    List<ItemList> getListsByCookie(String cookie) throws UnathorizedException, NotFoundException;

    ItemList createList(String cookie, ItemListDto list) throws UnathorizedException, NotFoundException;

    ItemList updateList(String id, String attributes) throws NotFoundException;

    void deleteList(String id) throws NotFoundException;
}
