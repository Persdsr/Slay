package com.slay.course.controller.course;

import com.slay.course.dto.request.category.SportCategoryRequest;
import com.slay.course.dto.response.category.CategoriesTagsDTO;
import com.slay.course.dto.response.category.SportCategoryDTO;
import com.slay.course.dto.response.course.CategoryAndPopularTagsDTO;
import com.slay.course.service.course.SportCategoryService;
import com.slay.course.service.course.TagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/category")
@AllArgsConstructor
@Validated
@Tag(name = "Sport Categories", description = "Спортивные категории")
public class SportCategoryController {

    private final SportCategoryService sportCategoryService;
    private final TagService tagService;

    @Operation(
            summary = "Получить список всех спортивных категорий",
            description = "Возвращает список всех спортивных категорий. Результаты кэшируются на 1 час для повышения производительности."
    )
    @GetMapping("/categories-name")
    public ResponseEntity<List<SportCategoryDTO>> getCategoriesName() {
        return new ResponseEntity<>(sportCategoryService.getCategories(), HttpStatus.OK);
    }

    @Operation(
            summary = "Получить список категорий и тегов",
            description = "Возвращает список всех спортивных категорий и топ-10 тегов. Результаты могут быть использованы для отображения на главной странице или в фильтрах."
    )
    @GetMapping("/categories-tags")
    public ResponseEntity<CategoriesTagsDTO> getCategoriesTagsNames() {
        return new ResponseEntity<>(sportCategoryService.getCategoriesTags(), HttpStatus.OK);
    }

    @Operation(
            summary = "Получить список категорий и популярных тегов с курсами",
            description = " Возвращает список всех спортивных категорий и популярных тегов с привязанными курсами. Результаты кэшируются на 20 минут для повышения производительности."
    )
    @GetMapping("/training-courses")
    public ResponseEntity<CategoryAndPopularTagsDTO> getCategoriesWithTrainingCourse() {
        return ResponseEntity.ok().body(tagService.getCategoryAndPopularTags());
    }

    @Operation(
            summary = "Создать новую спортивную категорию",
            description = "Создает новую спортивную категорию с указанными данными и загружает постер. Доступ к этому методу ограничен:" +
        "- Администраторы (`ADMIN`)."
    )
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping()
    public ResponseEntity<HttpStatus> createCategory(@RequestPart("categoryFields") @Valid SportCategoryRequest category,
                                       @RequestPart("poster") MultipartFile poster) {

        sportCategoryService.createCategory(category, poster);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(
            summary = "Удалить спортивную категорию",
            description = "Удаляет спортивную категорию по её идентификатору. Доступ к этому методу ограничен:" +
        "- Администраторы (`ADMIN`)."
    )
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteCategory(@PathVariable int id) {
        sportCategoryService.deleteCategory(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(
            summary = "Обновить данные спортивной категории",
            description = "Обновляет данные спортивной категории по её названию. Доступ к этому методу ограничен:" +
        "- Администраторы (`ADMIN`)."
    )
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{name}")
    public ResponseEntity<HttpStatus> updateCategory(@PathVariable("name") String name,
                                                      @RequestPart("categoryFields") SportCategoryDTO category,
                                                      @RequestPart(value = "poster", required = false) MultipartFile poster) {
        sportCategoryService.updateCategoryFields(name, category, poster);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
