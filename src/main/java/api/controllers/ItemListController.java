package api.controllers;

import dto.ItemListDto;
import entities.ItemList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import services.interfaces.IItemListService;


@RestController
@RequestMapping("/api/list")
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
    public ItemList create(@RequestBody ItemListDto itemListDTO) {
        return service.createItemList(itemListDTO);
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