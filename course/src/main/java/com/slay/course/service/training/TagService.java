package com.slay.course.service.training;

import com.slay.course.DTO.response.course.CategoryAndPopularTagsDTO;
import com.slay.course.entity.category.SportCategoryEntity;
import com.slay.course.entity.category.TagEntity;
import com.slay.course.repository.category.CategoryRepo;
import com.slay.course.repository.course.TagRepo;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TagService {

    private final TagRepo tagRepo;
    private final CategoryRepo categoryRepo;

    @Cacheable("categories-and-tags")
    public CategoryAndPopularTagsDTO getCategoryAndPopularTags()  {

        List<SportCategoryEntity> categories = categoryRepo.findAll();
        List<TagEntity> tags = tagRepo.findAll();

        return CategoryAndPopularTagsDTO.toModel(categories, tags);
    }
}
