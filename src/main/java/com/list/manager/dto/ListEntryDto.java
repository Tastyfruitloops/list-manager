package com.list.manager.dto;

public class ListEntryDto {

    private final Long listId;
    private final String text;


    public ListEntryDto(Long listId, String text) {
        this.listId = listId;
        this.text = text;
    }

    public Long getListId() {
        return listId;
    }

    public String getText() {
        return text;
    }
}
