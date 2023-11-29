package com.dev.listmanager.controller;

import com.dev.listmanager.dto.ItemDto;
import com.dev.listmanager.dto.ItemListDto;
import com.dev.listmanager.dto.TagDto;
import com.dev.listmanager.entity.Item;
import com.dev.listmanager.entity.ItemList;
import com.dev.listmanager.entity.Tag;
import com.dev.listmanager.exception.NotFoundException;
import com.dev.listmanager.exception.UnathorizedException;
import com.dev.listmanager.service.interfaces.IItemListService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<List<ItemList>> getMyLists(@CookieValue("token") String cookie) throws UnathorizedException, NotFoundException {
        List<ItemList> list = itemListService.getListsByCookie(cookie);
        return ResponseEntity.ok().body(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemList> getById(@PathVariable String id) throws NotFoundException {
        ItemList list = itemListService.getListById(id);
        return ResponseEntity.ok().body(list);
    }

    @PostMapping("/")
    public ResponseEntity<ItemList> createList(@CookieValue("token") String cookie, @RequestBody ItemListDto itemListDto) throws UnathorizedException, NotFoundException {
        ItemList list = itemListService.createList(cookie, itemListDto);
        return ResponseEntity.ok().body(list);
    }

    @PostMapping("/{id}/item")
    public ResponseEntity<Item> createItem(@PathVariable String id, @RequestBody ItemDto itemDto) throws NotFoundException {
        Item item = itemListService.addItem(id, itemDto);
        return ResponseEntity.ok().body(item);
    }

    @PostMapping("/{id}/tag")
    public ResponseEntity<ItemList> tagList(@PathVariable String id, @RequestBody TagDto tagDto) throws NotFoundException {
        itemListService.addTag(id, tagDto.getName());
        ItemList list = itemListService.getListById(id);
        return ResponseEntity.ok().body(list);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ItemList> updateList(@PathVariable String id, @RequestBody String attributes) throws NotFoundException {
        ItemList list = itemListService.updateList(id, attributes);
        return ResponseEntity.ok().body(list);
    }

    @PutMapping("/item/{id}")
    public ResponseEntity<Item> updateItem(@PathVariable String id, @RequestBody String attributes) throws NotFoundException {
        itemListService.updateItem(id, attributes);
        Item item = itemListService.getItemById(id);
        return ResponseEntity.ok().body(item);
    }

    @PutMapping("/tag/{id}")
    public ResponseEntity<Tag> updateTag(@PathVariable String id, @RequestBody TagDto tagDto) throws NotFoundException {
        itemListService.updateTag(id, tagDto.getName());
        Tag tag = itemListService.getTagById(id);
        return ResponseEntity.ok().body(tag);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteList(@PathVariable String id) throws NotFoundException {
        itemListService.deleteList(id);
        return ResponseEntity.ok().body("List was successfully deleted!");
    }

    @DeleteMapping("/item/{id}")
    public ResponseEntity<String> deleteItem(@PathVariable String id) throws NotFoundException {
        itemListService.deleteItem(id);
        return ResponseEntity.ok().body("Item was successfully deleted!");
    }

    @DeleteMapping("/tag/{id}")
    public ResponseEntity<String> deleteTag(@PathVariable String id) throws NotFoundException {
        itemListService.deleteTag(id);
        return ResponseEntity.ok().body("Tag was successfully deleted!");
    }
}
