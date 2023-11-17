package com.list.manager.services.interfaces;

import com.list.manager.dto.ListEntryDto;
import com.list.manager.dto.TagDto;
import com.list.manager.entities.ListEntry;

import java.util.List;

public interface IListEntryService {

    List <ListEntry> getAllEntries();

    ListEntry getEntryById(Long id);

    ListEntry createEntry(ListEntryDto entryDto);

    ListEntry updateEntry(Long id, String attributes);

    void deleteEntry(Long id);

    void tagEntry(Long id, TagDto tag);
    void untagEntry(Long id, TagDto tag);


}
