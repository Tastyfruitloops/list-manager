package com.list.manager.dto;

public class ItemListDto {
    private final Long id;
    private final Long userId;
    private boolean available;

    public ItemListDto(Long id, Long userId) {
        this.id = id;
        this.userId = userId;
        this.available = false;
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }


}
