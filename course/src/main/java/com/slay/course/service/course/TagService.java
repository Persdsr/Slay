package com.slay.course.service.course;

import com.slay.course.dto.response.course.CategoryAndPopularTagsDTO;
import com.slay.course.dto.response.course.CategoryWithTrainingCourseDTO;
import com.slay.course.dto.response.course.TagCoursesDTO;
import com.slay.course.dto.response.user.UserLiteDTO;
import com.slay.course.client.UserServiceClient;
import com.slay.course.entity.category.SportCategoryEntity;
import com.slay.course.entity.category.TagEntity;
import com.slay.course.entity.course.TrainingCourseEntity;
import com.slay.course.repository.category.CategoryRepo;
import com.slay.course.repository.course.TagRepo;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TagService {

    private final TagRepo tagRepo;
    private final CategoryRepo categoryRepo;
    private final UserServiceClient userServiceClient;

    @Cacheable("categories-and-tags")
    public CategoryAndPopularTagsDTO getCategoryAndPopularTags() {
        List<SportCategoryEntity> categories = categoryRepo.findAll();
        List<TagEntity> tags = tagRepo.findAll();

        Set<TrainingCourseEntity> allCourses = new HashSet<>();
        categories.forEach(category -> allCourses.addAll(category.getTrainingCourses()));
        tags.forEach(tag -> allCourses.addAll(tag.getTrainingCourses()));

        Set<Integer> authorIds = allCourses.stream()
                .map(TrainingCourseEntity::getAuthorId)
                .collect(Collectors.toSet());

        Map<Integer, UserLiteDTO> authorMap = userServiceClient.getUsersLiteBatch(new ArrayList<>(authorIds));

        List<CategoryWithTrainingCourseDTO> categoryDTOs = categories.stream()
                .map(category -> CategoryWithTrainingCourseDTO.toModel(category, authorMap))
                .collect(Collectors.toList());

        Set<TagCoursesDTO> tagDTOs = tags.stream()
                .map(tag -> TagCoursesDTO.toModel(tag, authorMap))
                .limit(2)
                .collect(Collectors.toSet());

        CategoryAndPopularTagsDTO dto = new CategoryAndPopularTagsDTO();
        dto.setCategories(categoryDTOs);
        dto.setTags(tagDTOs);
        return dto;
    }
}
