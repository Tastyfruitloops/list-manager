package com.dev.listmanager.controller;

import com.dev.listmanager.entity.User;
import com.dev.listmanager.exception.NotFoundException;
import com.dev.listmanager.service.interfaces.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @Operation(summary = "Get the current user")
    public ResponseEntity<User> getMe(@CookieValue("token") String cookie) throws NotFoundException {
        User user = service.getUserByUsername(cookie.split("&")[0]);
        return ResponseEntity.ok().body(user);
    }

    @GetMapping("/")
    @Operation(summary = "Get all users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> listUsers = service.getAllUsers();
        return ResponseEntity.ok().body(listUsers);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Successfully retrieved user"), @ApiResponse(responseCode = "404", description = "User not found") })
    public ResponseEntity<User> getUserById(@CookieValue("token") String cookie, @PathVariable String id) throws NotFoundException {
        String username = cookie.split("&")[0];
        User user = service.getUserById(username, id);
        return ResponseEntity.ok().body(user);
    }

    @PutMapping("/me")
    @Operation(summary = "Update the current user")
    public ResponseEntity<String> updateUser(@CookieValue("token") String cookie, @RequestBody String attributes) throws NotFoundException {
        String username = cookie.split("&")[0];
        service.updateUser(username, attributes);
        return ResponseEntity.ok().body("User was successfully updated!");
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user by ID")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Successfully deleted user"), @ApiResponse(responseCode = "404", description = "User not found") })
    public ResponseEntity<String> deleteUser(@PathVariable String id) throws NotFoundException {
        service.deleteUser(id);
        return ResponseEntity.ok().body("User was successfully deleted!");
    }
}
