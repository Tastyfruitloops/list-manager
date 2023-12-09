package com.dev.listmanager.dto;

import jakarta.validation.constraints.Pattern;

import java.util.List;

public class ItemDto {
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Name can only contain alphanumeric characters")
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
