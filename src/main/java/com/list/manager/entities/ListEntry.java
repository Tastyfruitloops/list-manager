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

    @Column(name = "text")
    private String text;

    @Column(name = "tag")
    private String tag;

    public ListEntry() {
    }

    public ListEntry(ItemList hostList, String text, String tag) {
        this.hostList = hostList;
        this.text = text;
        this.tag = tag;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDescription() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Long getId() {
        return Id;
    }

    @JsonIgnore
    public ItemList getHostList() {
        return hostList;
    }
}
