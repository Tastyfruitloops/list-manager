package com.list.manager.services;

import com.list.manager.repository.ListRepository;
import com.list.manager.entities.ItemList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.BasicJsonParser;
import org.springframework.stereotype.Service;
import com.list.manager.services.interfaces.IItemListService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ItemListService implements IItemListService {

    private final ListRepository repository;
    private final BasicJsonParser parser = new BasicJsonParser();

    @Autowired
    public ItemListService(ListRepository repository) {
        this.repository = repository;
    }

    @Override
    public List <ItemList> getAllItemLists() {
        return repository.findAll();
    }

    @Override
    public ItemList getItemListById(Long id) {
        Optional <ItemList> itemList = repository.findById(id);
        if (itemList.isPresent()) {
            return itemList.get();
        } else {
            throw new RuntimeException("List not found with id: " + id);
        }
    }

    @Override
    public ItemList createItemList(ItemList list) {
        return repository.save(list);
    }

    @Override
    public ItemList updateItemList(Long id, String attributes) {
        Optional <ItemList> DBItemList = repository.findById(id);
        if (DBItemList.isEmpty()) {
            throw new RuntimeException("Cannot update list that doesn't exist");
        }
        DBItemList.ifPresent(itemList -> {
                    Map <String, Object> map = parser.parseMap(attributes);
                    map.forEach((s, o) -> {
                        switch (s) {
                            case "name" -> itemList.setName(o.toString());
                        }
                    });
                }
        );

        return repository.save(DBItemList.get());
    }

    @Override
    public void deleteItemList(Long id) {
        if (repository.findById(id).isPresent()) {
            repository.deleteById(id);
        } else {
            throw new RuntimeException("Cannot delete list that doesn't exist");
        }
    }
}
