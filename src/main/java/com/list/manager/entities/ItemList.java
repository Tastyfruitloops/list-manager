package com.list.manager.entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "lists")
public class ItemList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private Long userId;

    @Column(name = "available")
    private boolean available;

    @Column(name = "archived")
    private boolean archived;

    @Column(name = "name")
    private String name;
    private UUID uuid;

    @OneToMany(mappedBy = "itemList", cascade = CascadeType.ALL, orphanRemoval = true)
    private List <ListEntry> entryList;

    public ItemList() {
    }

    public ItemList(String name, Long userId) {


        this.id = id;

        this.userId = userId;

        this.name = name;

        this.available = false;

        this.archived = false;

        this.entryList = new ArrayList <>();
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public List <ListEntry> getEntryList() {
        return entryList;
    }

    public List <ListEntry> getEntryDtoList() {
        return entryList;
    }

    public void addEntry(ListEntry entry) {
        this.entryList.add(entry);
    }

    public void deleteEntry(ListEntry entry) {
        this.entryList.remove(entry);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public boolean isArchived() {
        return archived;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }
}
