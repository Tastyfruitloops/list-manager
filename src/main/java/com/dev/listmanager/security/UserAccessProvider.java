package com.dev.listmanager.security;

import com.dev.listmanager.entity.Item;
import com.dev.listmanager.entity.ItemList;
import com.dev.listmanager.entity.Tag;
import com.dev.listmanager.exception.NotFoundException;
import com.dev.listmanager.service.ItemListService;
import com.dev.listmanager.service.UserService;
import com.dev.listmanager.util.RequestWrapper;
import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

@Component
public class UserAccessProvider {
    private final ItemListService listService;
    private final UserService userService;

    public UserAccessProvider(ItemListService listService, UserService userService) {
        this.listService = listService;
        this.userService = userService;
    }

    public boolean canViewLists(RequestWrapper request) {
        try {
            String username = getUsernameFromCookie(getCookieValue(request));
            var user = userService.getUserByUsernameOptional(username);
            return user.isPresent();
        } catch (NotFoundException e) {
            return false;
        }
    }

    private String getUsernameFromCookie(String cookie) {
        int idx = cookie.indexOf('&');
        return cookie.substring(0, idx);
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

    public boolean canModifyItem(RequestWrapper request) {
        String path = request.getServletPath();
        String uuid = path.substring(path.lastIndexOf("/") + 1).replace("/", "");

        try {
            Item item = listService.getItemById(uuid);
            ItemList list = item.getList();

            String username = getUsernameFromCookie(getCookieValue(request));

            return list.getOwner().getUsername().equals(username);
        } catch (NotFoundException e) {
            return false;
        }
    }

    public boolean canModifyTag(RequestWrapper request) {
        String path = request.getServletPath();
        String uuid = path.substring(path.lastIndexOf("/") + 1).replace("/", "");

        try {
            Tag tag = listService.getTagById(uuid);
            ItemList list = tag.getList();

            String username = getUsernameFromCookie(getCookieValue(request));

            return list.getOwner().getUsername().equals(username);
        } catch (NotFoundException e) {
            return false;
        }
    }

    public boolean canAccessList(RequestWrapper request) {
        String path = request.getServletPath();
        String uuid = path.substring(path.lastIndexOf("/") + 1).replace("/", "");

        try {
            return hasAccess(uuid, getUsernameFromCookie(getCookieValue(request)));
        } catch (NotFoundException e) {
            return false;
        }
    }

    private boolean hasAccess(String uuid, String username) throws NotFoundException {
        ItemList list = listService.getListById(uuid);
        return list.getOwner().getUsername().equals(username) || list.isPublic();
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

    private boolean canModify(String uuid, String username) throws NotFoundException {
        ItemList list = listService.getListById(uuid);
        return list.getOwner().getUsername().equals(username);
    }
}
