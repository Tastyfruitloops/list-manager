package com.list.manager.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "entries")
public class ListEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "itemList_id")
    private Long listId;

    @Column(name = "name")
    private String entryName;

    @Column(name = "description")
    private String description;

    public ListEntry() {
    }

    public ListEntry(Long listId, String entryName, String description) {
        this.listId = listId;
        this.entryName = entryName;
        this.description = description;
    }

    public String getEntryName() {
        return entryName;
    }

    public void setEntryName(String entryName) {
        this.entryName = entryName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getId() {
        return Id;
    }

    public Long getListId() {
        return listId;
    }
}
