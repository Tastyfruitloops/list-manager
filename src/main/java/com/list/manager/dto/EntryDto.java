package com.list.manager.dto;

public class EntryDto {

    private String listId;
    private String description;


    public EntryDto(String listId, String description) {
        this.listId = listId;
        this.description = description;
    }

    public String getListId() {
        return listId;
    }

    public void setListId(String listId) {
        this.listId = listId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
