package entities;

public class ListEntry {
    private String entryName;
    private String description;

    public ListEntry(String entryName, String description) {
        this.entryName = entryName;
        this.description = description;
    }

    public String getEntryName() {
        return entryName;
    }

    public String getDescription() {
        return description;
    }
}
