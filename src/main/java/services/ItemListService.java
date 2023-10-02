package services;

import dto.ItemListDto;
import entities.ItemList;
import services.interfaces.IItemListService;

import java.util.List;

public class ItemListService implements IItemListService {
    @Override
    public List <ItemList> getAllItemLists() {
        return null;
    }

    @Override
    public ItemList getItemListById(Long id) {
        return null;
    }

    @Override
    public ItemList createItemList(ItemListDto list) {
        return null;
    }

    @Override
    public ItemList updateItemList(Long id, String attributes) {
        return null;
    }

    @Override
    public void deleteItemList(Long id) {

    }
}
