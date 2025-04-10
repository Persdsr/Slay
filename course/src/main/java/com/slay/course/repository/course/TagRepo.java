package com.slay.course.repository.course;

import com.slay.course.entity.category.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;


public interface TagRepo extends JpaRepository<TagEntity, Integer> {
    List<TagEntity> findAllByNameIn(Set<String> names);
    List<TagEntity> findByNameContainingIgnoreCase(String name);
}
