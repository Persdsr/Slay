package com.slay.course.repository.category;

import com.slay.course.entity.category.SportCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepo extends JpaRepository<SportCategoryEntity, Integer> {
    Optional<SportCategoryEntity> findByName(String name);
}