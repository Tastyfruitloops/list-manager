package com.list.manager.services.interfaces;

import com.list.manager.dto.ItemListDto;
import com.list.manager.dto.TagDto;
import com.list.manager.entities.ItemList;

import java.util.List;

public interface IItemListService {

    List <ItemList> getAllItemLists();

    ItemList getItemListById(Long id);

    ItemList createItemList(ItemListDto list);

    ItemList updateItemList(Long id, String attributes);

    void deleteItemList(Long id);

    void tagList(Long id, TagDto tag);
    void untagList(Long id, TagDto tag);
}