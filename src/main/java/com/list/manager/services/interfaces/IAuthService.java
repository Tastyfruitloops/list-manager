package com.list.manager.services.interfaces;

import com.list.manager.dto.UserDto;
import com.list.manager.entities.CookieEntry;
import com.list.manager.entities.User;
import com.list.manager.exception.NotFoundException;

import java.util.Optional;

public interface IAuthService {
    public void addCookie(Long id, String cookie);

    public void deleteCookie(String cookie);

    public Optional <CookieEntry> getCookie(String cookie);

    public User authenticate(UserDto credentialDto) throws NotFoundException;

    public User findByUsername(String username) throws NotFoundException;

    public String createToken(User user);
}
