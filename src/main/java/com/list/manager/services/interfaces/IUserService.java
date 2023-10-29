package com.list.manager.services.interfaces;

import com.list.manager.entities.User;

import java.util.List;

public interface IUserService {

    List <User> getAllUsers();

    User getUserById(Long id);

    User getUserByUsername(String username);

    void openAccess(Long userId, Long listId);

    void closeAccess(Long userId, Long listId);

    void archiveList(Long userId, Long listId);

    void unarchiveList(Long userId, Long listId);

    User createUser(User user);

    User updateUser(Long id, String attributes);

    void deleteUser(Long id);
}
