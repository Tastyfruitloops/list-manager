package com.list.manager.services.interfaces;

import com.list.manager.dto.TagDto;
import com.list.manager.entities.Tag;

public interface ITagService {

    Tag getTagById(Long id);
    Tag createTag(TagDto tagDto);
    void deleteTag(Long id);
}
