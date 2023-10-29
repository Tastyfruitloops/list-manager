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
    @JoinColumn(name = "owner_id")
    private User owner;

    @Column(name = "available")
    private boolean available = false;

    @Column(name = "archived")
    private boolean archived = false;

    @Column(name = "name")
    private String name;

    private UUID uuid;

    @OneToMany(mappedBy = "hostList", cascade = CascadeType.ALL, orphanRemoval = true)
    private List <ListEntry> entryList = new ArrayList<>();

    public ItemList() {
    }

    public ItemList(String name, User owner) {

        this.owner = owner;

        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public User getOwner() {
        return owner;
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
