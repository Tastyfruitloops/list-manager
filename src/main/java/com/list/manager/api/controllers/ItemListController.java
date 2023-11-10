package com.list.manager.api.controllers;

import com.list.manager.dto.ItemListDto;
import com.list.manager.entities.ItemList;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.list.manager.services.interfaces.IItemListService;


@RestController
@RequestMapping("/api/list")
@Tag(name = "List Controller", description = "Operations related to list")
public class ItemListController implements IController <ItemList> {

    private final IItemListService service;

    @Autowired
    public ItemListController(IItemListService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get list by ID", description = "Retrieve details of a list by providing list ID.")
    public ItemList getById(@PathVariable @Parameter(description = "ID of the list") Long id) {
        return service.getItemListById(id);
    }

    @PostMapping("/")
    @Operation(summary = "Create list", description = "Create a new list.")
    public ItemList create(@RequestBody ItemListDto itemListDto) {
        return service.createItemList(itemListDto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update list by ID", description = "Update details of a list by providing list ID.")
    public ItemList update(@PathVariable @Parameter(description = "ID of the list") Long id, @RequestBody String attributes) {
        return service.updateItemList(id, attributes);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete list by ID", description = "Delete a list by providing list ID.")
    public void delete(@PathVariable @Parameter(description = "ID of the list") Long id) {
        service.deleteItemList(id);
    }
}