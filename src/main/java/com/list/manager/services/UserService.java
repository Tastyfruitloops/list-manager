package com.list.manager.services;

import com.list.manager.repository.UserRepository;
import com.list.manager.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.BasicJsonParser;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import com.list.manager.services.interfaces.IUserService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService implements IUserService {

    private final UserRepository repository;

    private final BasicJsonParser parser = new BasicJsonParser();

    @Autowired
    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public List <User> getAllUsers() {
        return repository.findAll();
    }

    @Override
    public User getUserById(Long id) {
        Optional <User> user = repository.findById(id);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new RuntimeException("User not found with id: " + id);
        }
    }

    @Override
    public User getUserByUsername(String username) {
        Optional <User> user = repository.findByUsername(username);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new RuntimeException("User not found with username: " + username);
        }
    }

    @Override
    public User createUser(User user) {
        return repository.save(user);
    }

    @Override
    public User updateUser(Long id, String attributes) {
        Optional <User> DBuser = repository.findById(id);
        if (DBuser.isEmpty()) {
            throw new RuntimeException("Cannot update user that doesn't exist");
        }
        DBuser.ifPresent(user -> {
                    Map <String, Object> map = parser.parseMap(attributes);
                    map.forEach((s, o) -> {
                        switch (s) {
                            case "username" -> user.setUsername(o.toString());
                            case "password" -> user.setPassword(o.toString());
                        }
                    });
                }
        );

        return repository.save(DBuser.get());
    }

    @Override
    public void deleteUser(Long id) {
        if (repository.findById(id).isPresent()) {
            repository.deleteById(id);
        } else {
            throw new RuntimeException("Cannot delete user that doesn't exist");
        }
    }
}
