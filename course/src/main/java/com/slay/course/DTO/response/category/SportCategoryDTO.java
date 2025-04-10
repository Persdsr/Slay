package com.slay.course.DTO.response.category;

import com.slay.course.entity.category.SportCategoryEntity;
import lombok.Data;

import java.io.Serializable;

@Data
public class SportCategoryDTO implements Serializable {
    private String name;
    private String description;
    private String poster;

    public static SportCategoryDTO toModel(SportCategoryEntity sportCategoryEntity) {
        SportCategoryDTO sportCategoryDTO = new SportCategoryDTO();
        sportCategoryDTO.setName(sportCategoryEntity.getName());
        sportCategoryDTO.setDescription(sportCategoryEntity.getDescription());
        sportCategoryDTO.setPoster(sportCategoryEntity.getPoster());
        return sportCategoryDTO;
    }
}
