package com.dev.listmanager.service.interfaces;

import com.dev.listmanager.dto.UserDto;
import com.dev.listmanager.entity.User;
import com.dev.listmanager.exception.NotFoundException;

import java.util.List;

public interface IUserService {
    List<User> getAllUsers();

    User getUserById(String requesterUsername, String id) throws NotFoundException;

    User getUserByUsername(String username) throws NotFoundException;

    User createUser(UserDto userDto);

    User updateUser(String id, String attributes) throws NotFoundException;

    void deleteUser(String id) throws NotFoundException;

    void createTemplateListForUser(User user);
}
