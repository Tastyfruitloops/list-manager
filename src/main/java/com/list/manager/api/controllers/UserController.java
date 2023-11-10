package com.list.manager.api.controllers;

import com.list.manager.entities.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.list.manager.services.interfaces.IUserService;

import java.util.List;

@RestController
@RequestMapping("/api/account")
@Tag(name="Account controller", description="Operations related to user accounts")
public class UserController implements IController <User> {

    private final IUserService service;

    @Autowired
    public UserController(IUserService service) {
        this.service = service;
    }

    @GetMapping("/me")
    @Operation(summary = "Get current user", description = "Retrieve details of the currently authenticated user.")
    public User getMe(@CookieValue("token") String cookie){
        return service.getUserById(Long.valueOf(cookie.split("&")[0]));
    }

    @GetMapping("/")
    @Operation(summary = "Get all users", description = "Retrieve a list of all users.")
    public List <User> getAll() {
        return service.getAllUsers();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID", description = "Retrieve details of a user by providing user ID.")
    public User getById(@PathVariable @Parameter(description = "ID of the user") Long id)  {
        return service.getUserById(id);
    }

    @PostMapping("/")
    @Operation(summary = "Create user", description = "Create a new user.")
    public User create(@RequestBody User user){
        return service.createUser(user);
    }

    @PostMapping("/me/open/{listId}")
    @Operation(summary = "Open access to list", description = "Open access to a specific list for the authenticated user.")
    public void openAccess(@CookieValue("token") String cookie, @PathVariable Long listId){
        service.openAccess(
                Long.valueOf(cookie.split("&")[0]),
                listId
        );
    }

    @PostMapping("/me/close/{listId}")
    @Operation(summary = "Close access to list", description = "Close access to a specific list for the authenticated user.")
    public void closeAccess(@CookieValue("token") String cookie, @PathVariable Long listId){
        service.closeAccess(
                Long.valueOf(cookie.split("&")[0]),
                listId
        );
    }

    @PostMapping("/me/archive/{listId}")
    @Operation(summary = "Archive list", description = "Archive a specific list for the authenticated user.")
    public void archiveList(@CookieValue("token") String cookie, @PathVariable Long listId){
        service.archiveList(
                Long.valueOf(cookie.split("&")[0]),
                listId
        );
    }

    @PostMapping("/me/unarchive/{listId}")
    @Operation(summary = "Unarchive list", description = "Unarchive a specific list for the authenticated user.")
    public void unarchiveList(@CookieValue("token") String cookie, @PathVariable Long listId){
        service.unarchiveList(
                Long.valueOf(cookie.split("&")[0]),
                listId
        );
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update user by ID", description = "Update details of a user by providing user ID.")
    public User update(@PathVariable @Parameter(description = "ID of the user") Long id, @RequestBody String attributes){
        return service.updateUser(id, attributes);
    }

    @PutMapping("/me")
    @Operation(summary = "Update current user", description = "Update details of the currently authenticated user.")
    public User updateMe(@CookieValue("token") String cookie, @RequestBody String attributes){
        var id = Long.valueOf(cookie.split("&")[0]);
        return service.updateUser(id, attributes);
    }

    @DeleteMapping("/me")
    @Operation(summary = "Delete current user", description = "Delete the currently authenticated user.")
    public void deleteMe(@CookieValue("token") String cookie){
        service.deleteUser(Long.valueOf(cookie.split("&")[0]));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user by ID", description = "Delete a user by providing user ID.")
    public void delete(@PathVariable @Parameter(description = "ID of the user") Long id) {
        service.deleteUser(id);
    }

}
