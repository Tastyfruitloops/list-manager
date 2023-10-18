package com.list.manager.api.controllers;

import com.list.manager.entities.ListEntry;
import com.list.manager.services.interfaces.IListEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/com/list/manager/list")
public class ListEntryController implements IController <ListEntry> {

    private final IListEntryService service;

    @Autowired
    public ListEntryController(IListEntryService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public ListEntry getById(@PathVariable Long id) {
        return service.getEntryById(id);
    }

    @PostMapping("/")
    public ListEntry create(@RequestBody ListEntry entry) {
        return service.createEntry(entry);
    }


    @PutMapping("/{id}")
    public ListEntry update(@PathVariable Long id, @RequestBody String attributes) {
        return service.updateEntry(id, attributes);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deleteEntry(id);
    }
}