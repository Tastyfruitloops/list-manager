package com.list.manager.services;

import com.list.manager.dto.ListEntryDto;
import com.list.manager.dto.TagDto;
import com.list.manager.entities.ItemList;
import com.list.manager.entities.Tag;
import com.list.manager.repository.ListRepository;
import com.list.manager.services.interfaces.IListEntryService;
import com.list.manager.entities.ListEntry;
import org.springframework.boot.json.BasicJsonParser;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.list.manager.repository.EntryRepository;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ListEntryService implements IListEntryService {

    private final EntryRepository repository;
    private final ListRepository listRepository;
    private final BasicJsonParser parser = new BasicJsonParser();

    public ListEntryService(EntryRepository repository, ListRepository listRepository) {
        this.repository = repository;
        this.listRepository = listRepository;
    }


    @Override
    public List <ListEntry> getAllEntries() {
        return repository.findAll();
    }

    @Override
    public ListEntry getEntryById(Long id) {
        Optional <ListEntry> user = repository.findById(id);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new RuntimeException("Entry not found with id: " + id);
        }
    }

    @Override
    public ListEntry createEntry(ListEntryDto entryDto) {
        var hostList = listRepository.findById(entryDto.getListId()).get();
        var entry = new ListEntry(hostList, entryDto.getName());
        return repository.save(entry);
    }

    @Override
    public ListEntry updateEntry(Long id, String attributes) {
        Optional <ListEntry> DBListEntry = repository.findById(id);
        if (DBListEntry.isEmpty()) {
            throw new RuntimeException("Cannot update entry that doesn't exist");
        }
        DBListEntry.ifPresent(entry -> {
                    var hostList = entry.getHostList();
                    if (hostList.isArchived()) {
                        throw new ResponseStatusException(HttpStatus.NOT_MODIFIED, "List is archived.");
                    }
                    Map <String, Object> map = parser.parseMap(attributes);
                    map.forEach((s, o) -> {
                        switch (s) {
                            case "name" -> entry.setText(o.toString());
                        }
                    });
                }
        );

        return repository.save(DBListEntry.get());
    }

    @Override
    public void deleteEntry(Long id) {
        if (repository.findById(id).isPresent()) {
            repository.deleteById(id);
        } else {
            throw new RuntimeException("Cannot delete entry that doesn't exist");
        }
    }


    @Override
    public void tagEntry(Long id, TagDto tagDto) {
        var entry = this.getEntryById(id);
        var optionalTag = findTagInList(entry.getHostList(), tagDto);
        optionalTag.ifPresent(entry::addTag);
    }

    @Override
    public void untagEntry(Long id, TagDto tagDto) {
        var entry = this.getEntryById(id);
        for (Tag tag: entry.getTagList()) {
            if (tag.getTag() == tagDto.getTag()) {
                entry.removeTag(tag);
                return;
            }
        }
    }

    private Optional <Tag> findTagInList(ItemList itemList, TagDto tagDto) {
        for (Tag tag : itemList.getListTags()) {
            if (tag.getTag() == tagDto.getTag()) {
                return Optional.of(tag);
            }
        }
        return Optional.empty();
    }
}
