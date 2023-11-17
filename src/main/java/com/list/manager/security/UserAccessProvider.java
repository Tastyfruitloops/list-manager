package com.list.manager.security;

import com.list.manager.entities.ItemList;
import com.list.manager.entities.ListEntry;
import com.list.manager.exception.UnauthorizedException;
import com.list.manager.services.ItemListService;
import com.list.manager.services.ListEntryService;
import com.list.manager.services.UserService;
import com.list.manager.util.RequestWrapper;
import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

@Component
public class UserAccessProvider {

    private final ItemListService listService;
    private final ListEntryService entryService;

    public UserAccessProvider(UserService userService, ItemListService listService, ListEntryService entryService) {
        this.listService = listService;
        this.entryService = entryService;
    }

    private boolean canAccessList(Long listId, Long userId) {
        ItemList itemList = listService.getItemListById(listId);
        return Objects.equals(itemList.getOwner().getId(), userId) || itemList.isShared();
    }

    private boolean canManageList(Long listId, Long userId) {
        ItemList itemList = listService.getItemListById(listId);
        return Objects.equals(itemList.getOwner().getId(), userId);
    }

    private boolean canAccessEntry(Long entryId, Long userId) {
        ListEntry entry = entryService.getEntryById(entryId);
        return canAccessList(entry.getHostList().getId(), userId);
    }

    private boolean canManageEntry(Long entryId, Long userId) {
        ListEntry entry = entryService.getEntryById(entryId);
        return canManageList(entry.getHostList().getId(), userId);
    }

    private Long getCookieValue(RequestWrapper request) throws UnauthorizedException {
        Optional <Cookie> cookie =
                Arrays.stream(request.getCookies()).filter(c -> Objects.equals(c.getName(), "token")).findFirst();

        if (cookie.isEmpty()) {
            throw new UnauthorizedException("Token cookie was not found");
        }

        String value = cookie.get().getValue();
        int index = value.indexOf('&');
        if (index != -1) {
            return Long.valueOf(value.substring(0, index));
        } else {
            throw new UnauthorizedException("Invalid cookie");
        }
    }

    public boolean canAccessList(RequestWrapper request) {
        String header = request.getServletPath();
        String cutHeader = header.substring(header.lastIndexOf("list/") + 5);
        int lastSlash = cutHeader.indexOf('/');
        Long listId = lastSlash >= 0 ? Long.valueOf(cutHeader.substring(0, lastSlash)) : Long.valueOf(cutHeader);


        try {
            Long userId = getCookieValue(request);
            return canAccessList(listId, userId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean canManageList(RequestWrapper request) {
        String header = request.getServletPath();
        String cutHeader = header.substring(header.lastIndexOf("list/") + 5);
        int lastSlash = cutHeader.indexOf('/');
        Long listId = lastSlash >= 0 ? Long.valueOf(cutHeader.substring(0, lastSlash)) : Long.valueOf(cutHeader);


        try {
            Long userId = getCookieValue(request);
            return canManageList(listId, userId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean canAccessEntry(RequestWrapper request) {
        String header = request.getServletPath();
        String entryId = header.substring(header.lastIndexOf('/') + 1);
        try {
            Long userId = getCookieValue(request);
            return canAccessEntry(Long.valueOf(entryId), userId);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean canManageEntry(RequestWrapper request) {
        String header = request.getServletPath();
        String entryId = header.substring(header.lastIndexOf('/') + 1);
        try {
            Long userId = getCookieValue(request);
            return canManageEntry(Long.valueOf(entryId), userId);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }



}
