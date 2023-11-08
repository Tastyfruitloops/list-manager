package com.list.manager.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "lists")
public class ItemList {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private User owner;

    @Column(name = "shared")
    private boolean shared;

    @Column(name = "archived")
    private boolean archived;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "hostList", cascade = CascadeType.ALL, orphanRemoval = true)
    private List <ListEntry> entryList = new ArrayList<>();

    public ItemList() {
    }

    public ItemList(String name, User owner) {
        this.owner = owner;
        this.name = name;
        this.shared = false;
        this.archived = false;
    }

    public UUID getId() {
        return id;
    }

    @JsonIgnore
    public User getOwner() {
        return owner;
    }

    public List <ListEntry> getEntryList() {
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

    public boolean isShared() {
        return shared;
    }

    public void setShared(boolean shared) {
        this.shared = shared;
    }

    public boolean isArchived() {
        return archived;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }
}
