package com.list.manager.api.controllers;

import com.list.manager.dto.ListEntryDto;
import com.list.manager.dto.TagDto;
import com.list.manager.entities.ListEntry;
import com.list.manager.services.interfaces.IListEntryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/entry")
@Tag(name="List entry controller", description="Operations related to list entries")
public class ListEntryController implements IController <ListEntry> {

    private final IListEntryService service;

    @Autowired
    public ListEntryController(IListEntryService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get list entry by ID", description = "Retrieve details of a list entry by providing entry ID.")
    public ListEntry getById(@PathVariable @Parameter(description = "ID of the list entry") Long id) {
        return service.getEntryById(id);
    }

    @PostMapping("/")
    @Operation(summary = "Create list entry", description = "Create a new list entry.")
    public ListEntry create(@RequestBody ListEntryDto entry) {
        return service.createEntry(entry);
    }

    @PostMapping("/{id}/tag")
    public void tagEntry(@PathVariable Long id, @RequestBody TagDto tagDto) {
        service.tagEntry(id, tagDto);
    }

    @PostMapping("/{id}/untag")
    public void untagEntry(@PathVariable Long id, @RequestBody TagDto tagDto) {
        service.untagEntry(id, tagDto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update list entry by ID", description = "Update details of a list entry by providing entry ID.")
    public ListEntry update(@PathVariable @Parameter(description = "ID of the list entry") Long id, @RequestBody String attributes) {
        return service.updateEntry(id, attributes);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete list entry by ID", description = "Delete a list entry by providing entry ID.")
    public void delete(@PathVariable @Parameter(description = "ID of the list entry") Long id) {
        service.deleteEntry(id);
    }
}