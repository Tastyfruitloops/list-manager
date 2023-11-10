package com.list.manager.dto;

public class ListEntryDto {

    private final Long listId;
    private final String description;
    private final String name;


    public ListEntryDto(Long listId, String name, String description) {
        this.listId = listId;
        this.description = description;
        this.name = name;
    }

    public Long getListId() {
        return listId;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }
}
