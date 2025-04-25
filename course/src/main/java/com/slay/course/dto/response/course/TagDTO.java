package com.slay.course.dto.response.course;

import com.slay.course.entity.category.TagEntity;
import lombok.Data;

import java.io.Serializable;

@Data
public class TagDTO implements Serializable {
    private int id;
    private String name;

    public static TagDTO toModel(TagEntity tagEntity) {
        TagDTO tagDTO = new TagDTO();
        tagDTO.setId(tagEntity.getId());
        tagDTO.setName(tagEntity.getName());
        return tagDTO;
    }

}
