package com.list.manager.services;

import com.list.manager.services.interfaces.IListEntryService;
import com.list.manager.entities.ListEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.BasicJsonParser;
import org.springframework.stereotype.Service;
import com.list.manager.repository.EntryRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ListEntryService implements IListEntryService {

    private final EntryRepository repository;
    private final BasicJsonParser parser = new BasicJsonParser();

    public ListEntryService(EntryRepository repository) {
        this.repository = repository;
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
    public ListEntry createEntry(ListEntry entry) {
        return repository.save(entry);
    }

    @Override
    public ListEntry updateEntry(Long id, String attributes) {
        Optional <ListEntry> DBListEntry = repository.findById(id);
        if (DBListEntry.isEmpty()) {
            throw new RuntimeException("Cannot update entry that doesn't exist");
        }
        DBListEntry.ifPresent(entry -> {
                    Map <String, Object> map = parser.parseMap(attributes);
                    map.forEach((s, o) -> {
                        switch (s) {
                            case "name" -> entry.setEntryName(o.toString());
                            case "description" -> entry.setDescription(o.toString());
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
}
