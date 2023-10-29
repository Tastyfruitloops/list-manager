package com.list.manager.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "entries")
public class ListEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "itemList_id")
    private ItemList hostList;

    @Column(name = "name")
    private String entryName;

    @Column(name = "description")
    private String description;

    public ListEntry() {
    }

    public ListEntry(ItemList hostList, String entryName, String description) {
        this.hostList = hostList;
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

    @JsonIgnore
    public ItemList getHostList() {
        return hostList;
    }
}
