package com.list.manager.services.interfaces;

import com.list.manager.entities.User;

import java.util.List;

public interface IUserService {

    List <User> getAllUsers();

    User getUserById(Long id);

    User getUserByUsername(String username);

    User createUser(User user);

    User updateUser(Long id, String attributes);

    void deleteUser(Long id);
}
