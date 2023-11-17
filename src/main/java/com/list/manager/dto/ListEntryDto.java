package com.list.manager.dto;

public class ListEntryDto {

    private final Long listId;
    private final String name;


    public ListEntryDto(Long listId, String name) {
        this.listId = listId;
        this.name = name;
    }

    public Long getListId() {
        return listId;
    }

    public String getName() {
        return name;
    }
}
