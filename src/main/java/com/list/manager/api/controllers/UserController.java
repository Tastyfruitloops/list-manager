package com.list.manager.api.controllers;

import com.list.manager.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.list.manager.services.interfaces.IUserService;

import java.util.List;

@RestController
@RequestMapping("/com/list/manager/account")
public class UserController implements IController <User> {

    private final IUserService service;

    @Autowired
    public UserController(IUserService service) {
        this.service = service;
    }

    @GetMapping("/me")
    public User getMe(@CookieValue("token") String cookie){
        return service.getUserById(Long.valueOf(cookie.split("&")[0]));
    }

    @GetMapping("/")
    public List <User> getAll() {
        return service.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getById(@PathVariable Long id) {
        return service.getUserById(id);
    }

    @PostMapping("/")
    public User create(@RequestBody User user){
        return service.createUser(user);
    }

    @PostMapping("/me/open/{listId}")
    public void openAccess(@CookieValue("token") String cookie, @PathVariable Long listId){
        service.openAccess(
                Long.valueOf(cookie.split("&")[0]),
                listId
        );
    }

    @PostMapping("/me/close/{listId}")
    public void closeAccess(@CookieValue("token") String cookie, @PathVariable Long listId){
        service.closeAccess(
                Long.valueOf(cookie.split("&")[0]),
                listId
        );
    }

    @PostMapping("/me/archive/{listId}")
    public void archiveList(@CookieValue("token") String cookie, @PathVariable Long listId){
        service.archiveList(
                Long.valueOf(cookie.split("&")[0]),
                listId
        );
    }

    @PostMapping("/me/unarchive/{listId}")
    public void unarchiveList(@CookieValue("token") String cookie, @PathVariable Long listId){
        service.unarchiveList(
                Long.valueOf(cookie.split("&")[0]),
                listId
        );
    }

    @PutMapping("/{id}")
    public User update(@PathVariable Long id, @RequestBody String attributes){
        return service.updateUser(id, attributes);
    }

    @PutMapping("/me")
    public User updateMe(@CookieValue("token") String cookie, @RequestBody String attributes){
        var id = Long.valueOf(cookie.split("&")[0]);
        return service.updateUser(id, attributes);
    }

    @DeleteMapping("/me")
    public void deleteMe(@CookieValue("token") String cookie){
        service.deleteUser(Long.valueOf(cookie.split("&")[0]));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deleteUser(id);
    }

}
