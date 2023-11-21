package com.dev.listmanager.dto;

import java.util.List;

public class ItemDto {
    private String name;
    private List<String> tagIds;

    public ItemDto() {
    }

    public ItemDto(String name, List<String> tagIds) {
        this.name = name;
        this.tagIds = tagIds;
    }

    public String getName() {
        return name;
    }

    public List<String> getTagIds() {
        return tagIds;
    }
}
