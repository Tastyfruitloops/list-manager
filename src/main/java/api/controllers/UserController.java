package api.controllers;

import entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import services.interfaces.IUserService;

import java.util.List;

@RestController
@RequestMapping("/api/account")
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

    @PutMapping("/{id}")
    public User update(@PathVariable Long id, @RequestBody String attributes){
        return service.updateUser(id, attributes);
    }

    @PutMapping("/me")
    public User updateMe(@CookieValue("token") String cookie, @RequestBody String attributes){
        return service.updateUser(Long.valueOf(cookie.split("&")[0]), attributes);
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
