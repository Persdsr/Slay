package com.slay.course.service.training;

import com.slay.course.DTO.request.category.SportCategoryRequest;
import com.slay.course.DTO.response.category.CategoriesTagsDTO;
import com.slay.course.DTO.response.category.SportCategoryDTO;
import com.slay.course.entity.category.SportCategoryEntity;
import com.slay.course.entity.category.TagEntity;
import com.slay.course.exception.CategoryNotFoundException;
import com.slay.course.repository.category.CategoryRepo;
import com.slay.course.repository.course.TagRepo;
import com.slay.course.service.file.FileService;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SportCategoryService {

    private final CategoryRepo categoryRepo;
    private final TagRepo tagRepo;
    private final FileService fileService;

    @Cacheable("categories")
    public List<SportCategoryDTO> getCategories() {
        return categoryRepo.findAll().stream().map(SportCategoryDTO::toModel).collect(Collectors.toList());
    }

    public CategoriesTagsDTO getCategoriesTags() {
        List<SportCategoryEntity> categories = categoryRepo.findAll();

        List<TagEntity> tags = tagRepo.findAll().stream().limit(10).toList();

        CategoriesTagsDTO categoriesTagsDTO = new CategoriesTagsDTO();
        categoriesTagsDTO.setCategories(categories.stream()
                .map(SportCategoryDTO::toModel)
                .collect(Collectors.toList())
        );
        categoriesTagsDTO.setTags(tags.stream()
                .map(TagEntity::getName)
                .collect(Collectors.toSet())
        );

        return categoriesTagsDTO;
    }

    @CacheEvict("categories")
    public void createCategory(SportCategoryRequest category, MultipartFile poster) {
            categoryRepo.save(
                    SportCategoryEntity.builder()
                            .name(category.getName())
                            .description(category.getDescription())
                            .poster(fileService.saveFile(poster))
                            .build()
            );

    }

    @CacheEvict("categories")
    public void deleteCategory(int categoryId) {
        categoryRepo.findById(categoryId).orElseThrow(
                () -> CategoryNotFoundException.builder().build()
        );

        categoryRepo.deleteById(categoryId);
    }

    @CacheEvict("categories")
    public void updateCategoryFields(String name, SportCategoryDTO category, MultipartFile poster) {
        SportCategoryEntity categoryEntity = categoryRepo.findByName(name).orElseThrow(
                () -> CategoryNotFoundException.builder().build()
        );

        categoryEntity.setName(category.getName());
        categoryEntity.setDescription(category.getDescription());
        if (category.getPoster() != null) {
            categoryEntity.setPoster(fileService.saveFile(poster));
        }
        categoryRepo.save(categoryEntity);
    }
}
