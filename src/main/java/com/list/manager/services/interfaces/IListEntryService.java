package com.list.manager.services.interfaces;

import com.list.manager.entities.ListEntry;

import java.util.List;

public interface IListEntryService {

    List <ListEntry> getAllEntries();

    ListEntry getEntryById(Long id);

    ListEntry createEntry(ListEntry entry);

    ListEntry updateEntry(Long id, String attributes);

    void deleteEntry(Long id);

}
