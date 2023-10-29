package com.list.manager.dto;

public class ItemListDto {
    private final String name;
    private final String owner;

    public ItemListDto(String name, String owner) {
        this.name = name;
        this.owner = owner;
    }

    public String getName() {
        return name;
    }

    public String getOwner() {
        return owner;
    }
}
