package com.dev.listmanager.service.interfaces;

import com.dev.listmanager.dto.UserDto;
import com.dev.listmanager.entity.User;
import com.dev.listmanager.entity.UserCookie;
import com.dev.listmanager.exception.NotFoundException;

import java.util.Optional;

public interface IAuthService {
    void addCookie(String cookie);
    void deleteCookie(String cookie);
    Optional<UserCookie> findCookie(String cookie);
    User authenticate(UserDto userDto) throws NotFoundException;
    User findByUsername(String username) throws NotFoundException;
    String createToken(User user);
}
