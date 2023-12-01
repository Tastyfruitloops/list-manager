package com.dev.listmanager.controller;

import com.dev.listmanager.dto.ItemDto;
import com.dev.listmanager.dto.ItemListDto;
import com.dev.listmanager.dto.TagDto;
import com.dev.listmanager.entity.Item;
import com.dev.listmanager.entity.ItemList;
import com.dev.listmanager.entity.Tag;
import com.dev.listmanager.exception.ListArchivedException;
import com.dev.listmanager.exception.NotFoundException;
import com.dev.listmanager.exception.UnathorizedException;
import com.dev.listmanager.service.interfaces.IItemListService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/lists")
public class ListController {
    private final IItemListService itemListService;

    @Autowired
    public ListController(IItemListService itemListService) {
        this.itemListService = itemListService;
    }

    @GetMapping("/")
    @Operation(summary = "Get lists of the currently authenticated user")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Lists retrieved successfully."), @ApiResponse(responseCode = "401", description = "Unauthorized"), @ApiResponse(responseCode = "404", description = "Lists not found"), @ApiResponse(responseCode = "500", description = "Internal Server Error") })
    public ResponseEntity<List<ItemList>> getMyLists(@CookieValue("token") String cookie) throws UnathorizedException, NotFoundException {
        List<ItemList> list = itemListService.getListsByCookie(cookie);
        return ResponseEntity.ok().body(list);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a list by ID")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "List retrieved successfully."), @ApiResponse(responseCode = "401", description = "Unauthorized"), @ApiResponse(responseCode = "404", description = "List not found"), @ApiResponse(responseCode = "500", description = "Internal Server Error") })
    public ResponseEntity<ItemList> getById(@PathVariable String id) throws NotFoundException {
        ItemList list = itemListService.getListById(id);
        return ResponseEntity.ok().body(list);
    }

    @PostMapping("/")
    @Operation(summary = "Create a new list")
    @ApiResponses(value = { @ApiResponse(responseCode = "201", description = "List created successfully."), @ApiResponse(responseCode = "401", description = "Unauthorized"), @ApiResponse(responseCode = "404", description = "User not found"), @ApiResponse(responseCode = "500", description = "Internal Server Error") })
    public ResponseEntity<ItemList> createList(@CookieValue("token") String cookie, @RequestBody ItemListDto itemListDto) throws UnathorizedException, NotFoundException {
        ItemList list = itemListService.createList(cookie, itemListDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(list);
    }

    @PostMapping("/{id}/item")
    @Operation(summary = "Create a new item in a list")
    @ApiResponses(value = { @ApiResponse(responseCode = "201", description = "Item created successfully."), @ApiResponse(responseCode = "404", description = "List not found"), @ApiResponse(responseCode = "500", description = "Internal Server Error") })
    public ResponseEntity<Item> createItem(@PathVariable String id, @RequestBody ItemDto itemDto) throws ListArchivedException {
        Item item = itemListService.addItem(id, itemDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(item);
    }

    @PostMapping("/{id}/tag")
    @Operation(summary = "Add a tag to a list by ID")
    @ApiResponses(value = { @ApiResponse(responseCode = "201", description = "Tag added successfully."), @ApiResponse(responseCode = "404", description = "List not found"), @ApiResponse(responseCode = "500", description = "Internal Server Error") })
    public ResponseEntity<Tag> tagList(@PathVariable String id, @RequestBody TagDto tagDto) throws NotFoundException, ListArchivedException {
        Tag tag = itemListService.addTag(id, tagDto.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(tag);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a list by ID")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "List updated successfully."), @ApiResponse(responseCode = "404", description = "List not found"), @ApiResponse(responseCode = "500", description = "Internal Server Error") })
    public ResponseEntity<ItemList> updateList(@PathVariable String id, @RequestBody String attributes) throws NotFoundException {
        ItemList list = itemListService.updateList(id, attributes);
        return ResponseEntity.ok().body(list);
    }

    @PutMapping("/item/{id}")
    @Operation(summary = "Update an item by ID")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Item updated successfully."), @ApiResponse(responseCode = "404", description = "Item not found"), @ApiResponse(responseCode = "500", description = "Internal Server Error") })
    public ResponseEntity<Item> updateItem(@PathVariable String id, @RequestBody String attributes) throws NotFoundException, ListArchivedException {
        Item item = itemListService.updateItem(id, attributes);
        return ResponseEntity.ok().body(item);
    }

    @PutMapping("/tag/{id}")
    @Operation(summary = "Update a tag by ID")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Tag updated successfully."), @ApiResponse(responseCode = "404", description = "Tag not found"), @ApiResponse(responseCode = "500", description = "Internal Server Error") })
    public ResponseEntity<Tag> updateTag(@PathVariable String id, @RequestBody TagDto tagDto) throws NotFoundException, ListArchivedException {
        Tag tag = itemListService.updateTag(id, tagDto.getName());
        return ResponseEntity.ok().body(tag);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a list by ID")
    @ApiResponses(value = { @ApiResponse(responseCode = "204", description = "List deleted successfully."), @ApiResponse(responseCode = "404", description = "List not found"), @ApiResponse(responseCode = "500", description = "Internal Server Error") })
    public ResponseEntity<String> deleteList(@PathVariable String id) throws NotFoundException {
        itemListService.deleteList(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/item/{id}")
    @Operation(summary = "Delete an item by ID")
    @ApiResponses(value = { @ApiResponse(responseCode = "204", description = "Item deleted successfully."), @ApiResponse(responseCode = "404", description = "Item not found"), @ApiResponse(responseCode = "500", description = "Internal Server Error") })
    public ResponseEntity<String> deleteItem(@PathVariable String id) throws NotFoundException, ListArchivedException {
        itemListService.deleteItem(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/tag/{id}")
    @Operation(summary = "Delete a tag by ID")
    @ApiResponses(value = { @ApiResponse(responseCode = "204", description = "Tag deleted successfully."), @ApiResponse(responseCode = "404", description = "Tag not found"), @ApiResponse(responseCode = "500", description = "Internal Server Error") })
    public ResponseEntity<String> deleteTag(@PathVariable String id) throws NotFoundException, ListArchivedException {
        itemListService.deleteTag(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
