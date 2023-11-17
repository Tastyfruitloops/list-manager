package com.list.manager.dto;

public class TagDto {

    private final Long listId;
    private final String tag;


    public TagDto(Long listId, String tag) {
        this.listId = listId;
        this.tag = tag;
    }

    public Long getListId() {
        return listId;
    }

    public String getTag() {
        return tag;
    }
}
