package com.dev.listmanager.controller;

import com.dev.listmanager.entity.User;
import com.dev.listmanager.exception.NotFoundException;
import com.dev.listmanager.service.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {
    private final IUserService service;

    @Autowired
    public UserController(IUserService service) {
        this.service = service;
    }

    @GetMapping("/me")
    public ResponseEntity<User> getMe(@CookieValue("token") String cookie) throws NotFoundException {
        User user = service.getUserByUsername(cookie.split("&")[0]);
        return ResponseEntity.ok().body(user);
    }
    @GetMapping("/")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> listUsers = service.getAllUsers();
        return ResponseEntity.ok().body(listUsers);
    }
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable String id) throws NotFoundException {
        User user = service.getUserById(id);
        return ResponseEntity.ok().body(user);
    }
    @PutMapping("/me")
    public ResponseEntity<String> updateUser(@CookieValue("token") String cookie, @RequestBody String attributes) throws NotFoundException {
        String username = cookie.split("&")[0];
        service.updateUser(username, attributes);
        return ResponseEntity.ok().body("User was successfully updated!");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable String id) throws NotFoundException {
        service.deleteUser(id);
        return ResponseEntity.ok().body("User was successfully deleted!");
    }
}
