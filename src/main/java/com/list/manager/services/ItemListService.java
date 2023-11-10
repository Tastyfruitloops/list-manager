package com.list.manager.services;

import com.list.manager.dto.ItemListDto;
import com.list.manager.repository.ListRepository;
import com.list.manager.entities.ItemList;
import com.list.manager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.BasicJsonParser;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.list.manager.services.interfaces.IItemListService;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ItemListService implements IItemListService {

    private final ListRepository repository;
    private final UserRepository userRepository;
    private final BasicJsonParser parser = new BasicJsonParser();

    @Autowired
    public ItemListService(ListRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    @Override
    public List <ItemList> getAllItemLists() {
        return repository.findAll();
    }

    @Override
    public ItemList getItemListById(Long id) {
        Optional <ItemList> itemList = repository.findById(id);
        if(!itemList.get().isShared()){
            throw new ResponseStatusException(HttpStatus.NOT_MODIFIED,"List is not shared.");
        }
        if (itemList.isPresent()) {
            return itemList.get();
        } else {
            throw new RuntimeException("List not found with id: " + id);
        }
    }

    @Override
    public ItemList createItemList(ItemListDto list) {
        var owner = userRepository.findByUsername(list.getOwner()).get();
        var listDB = new ItemList(list.getName(), owner);
        return repository.save(listDB);
    }

    @Override
    public ItemList updateItemList(Long id, String attributes) {
        Optional <ItemList> DBItemList = repository.findById(id);
        if (DBItemList.isEmpty()) {
            throw new RuntimeException("Cannot update list that doesn't exist");
        }
        DBItemList.ifPresent(itemList -> {
                    if(itemList.isArchived()){
                        throw new ResponseStatusException(HttpStatus.NOT_MODIFIED,"List is archived.");
                    }
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
