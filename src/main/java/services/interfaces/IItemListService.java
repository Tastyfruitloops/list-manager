package services.interfaces;

import dto.ItemListDto;
import entities.ItemList;

import java.util.List;

public interface IItemListService {
    List <ItemList> getAllItemLists();

    ItemList getItemListById(Long id);

    ItemList createItemList(ItemListDto list);

    ItemList updateItemList(Long id, String attributes);

    void deleteItemList(Long id);
}