package com.list.manager.services;

import com.list.manager.dto.TagDto;
import com.list.manager.entities.Tag;
import com.list.manager.repository.ListRepository;
import com.list.manager.repository.TagRepository;
import com.list.manager.services.interfaces.ITagService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;


@Service
public class TagService implements ITagService {

    private final TagRepository repository;
    private final ListRepository listRepository;

    @Autowired
    public TagService(TagRepository repository, ListRepository listRepository) {
        this.repository = repository;
        this.listRepository = listRepository;
    }

    @Override
    public Tag getTagById(Long id) {
        var optionalTag = repository.findById(id);
        if (optionalTag.isPresent()) {
            return optionalTag.get();
        } else {
            throw new RuntimeException("Tag not found");
        }
    }

    @Override
    public Tag createTag(TagDto tagDto) {
        var hostList = listRepository.findById(tagDto.getListId()).get();
        var tagDB = new Tag(tagDto.getTag(), hostList);
        return repository.save(tagDB);
    }

    @Override
    public void deleteTag(Long id) {
        if (repository.findById(id).isPresent()) {
            repository.deleteById(id);
        } else {
            throw new RuntimeException("Cannot delete tag that doesn't exist");
        }
    }
}
