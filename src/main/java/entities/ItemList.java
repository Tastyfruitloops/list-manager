package entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "lists")
public class ItemList {

    @Id
    private Long id;
    private List <ListEntry> entryList;

    public ItemList() {
    }

    public ItemList(Long id, List <ListEntry> entryList) {
        this.id = id;
        this.entryList = entryList;
    }

    public Long getId() {
        return id;
    }

    public List <ListEntry> getEntryList() {
        return entryList;
    }

    public void setEntryList(List <ListEntry> entryList) {
        this.entryList = entryList;
    }

    public void addEntry(ListEntry entry) {
        this.entryList.add(entry);
    }
}
