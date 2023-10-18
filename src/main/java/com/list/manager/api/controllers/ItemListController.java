package com.list.manager.api.controllers;

import com.list.manager.entities.ItemList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.list.manager.services.interfaces.IItemListService;


@RestController
@RequestMapping("/com/list/manager/list")
public class ItemListController implements IController <ItemList> {

    private final IItemListService service;

    @Autowired
    public ItemListController(IItemListService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public ItemList getById(@PathVariable Long id) {
        return service.getItemListById(id);
    }

    @PostMapping("/")
    public ItemList create(@RequestBody ItemList itemList) {
        return service.createItemList(itemList);
    }

    @PutMapping("/{id}")
    public ItemList update(@PathVariable Long id, @RequestBody String attributes) {
        return service.updateItemList(id, attributes);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deleteItemList(id);
    }
}