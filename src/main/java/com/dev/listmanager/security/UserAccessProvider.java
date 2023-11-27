package com.dev.listmanager.security;

import com.dev.listmanager.entity.ItemList;
import com.dev.listmanager.exception.NotFoundException;
import com.dev.listmanager.service.ItemListService;
import com.dev.listmanager.util.RequestWrapper;
import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

@Component
public class UserAccessProvider {
    private final ItemListService listService;

    public UserAccessProvider(ItemListService listService) {
        this.listService = listService;
    }

    private boolean hasAccess(String uuid, String username) throws NotFoundException {
        ItemList list = listService.getListById(uuid);
        return list.getOwner().getUsername().equals(username) || list.isPublic();
    }

    private boolean canModify(String uuid, String username) throws NotFoundException {
        ItemList list = listService.getListById(uuid);
        return list.getOwner().getUsername().equals(username);
    }

    private String getCookieValue(RequestWrapper request) throws NotFoundException {
        Optional<Cookie> cookie = Arrays.stream(request.getCookies()).filter(c -> Objects.equals(c.getName(), "token")).findFirst();

        if (cookie.isEmpty()) {
            throw new NotFoundException("Token cookie was not found");
        }

        String value = cookie.get().getValue();
        if (!value.contains("&")) {
            throw new NotFoundException("Token cookie was not found");
        }

        return value.substring(0, value.length() - 1);
    }

    public boolean canAccessList(RequestWrapper request) {
        String path = request.getServletPath();
        String uuid = path.substring(path.lastIndexOf("/") + 1).replace("/", "");

        if (uuid.equals("me")) {
            return true;
        }

        try {
            return hasAccess(uuid, getUsernameFromCookie(getCookieValue(request)));
        } catch (NotFoundException e) {
            return false;
        }
    }

    public boolean canModifyList(RequestWrapper request) {
        String path = request.getServletPath();
        String uuid = path.substring(path.lastIndexOf("/") + 1).replace("/", "");

        try {
            return canModify(uuid, getUsernameFromCookie(getCookieValue(request)));
        } catch (NotFoundException e) {
            return false;
        }
    }

    private String getUsernameFromCookie(String cookie) {
        int idx = cookie.indexOf('&');
        return cookie.substring(0, idx);
    }
}
