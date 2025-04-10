package com.slay.course.controller.course;

import com.slay.course.DTO.response.course.CourseResponse;
import com.slay.course.DTO.response.course.SearchDTO;
import com.slay.course.DTO.response.course.TrainingCourseLiteDTO;
import com.slay.course.service.training.TrainingCourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequestMapping("/api/training-course")
@Slf4j
@AllArgsConstructor
@Tag(name = "Training course", description = "Тренировочные курсы")
public class TrainingCourseController {

    private final TrainingCourseService trainingCourseService;

    @Operation(
            summary = "Получить детали курса по его идентификатору",
            description = "Возвращает детали курса по его идентификатору. Если пользователь является покупателем курса или его автором, возвращается полная информация о курсе. В противном случае возвращается сокращённая информация."
    )
    @GetMapping("/detail/{course-id}")
    public ResponseEntity<CourseResponse> getTrainingCourseDetail(@AuthenticationPrincipal UserDetails userDetails, @PathVariable("course-id") int courseId) {
        return new ResponseEntity<>(trainingCourseService.getTrainingCourseDetail(userDetails, courseId), HttpStatus.OK);
    }

    @Operation(
            summary = "Поиск курсов, категорий, тегов и авторов по запросу",
            description = "Возвращает результаты поиска по запросу, включая курсы, категории, теги и авторов. Поиск осуществляется с использованием алгоритма Левенштейна для нахождения похожих результатов."
    )
    @GetMapping("/search")
    public ResponseEntity<SearchDTO> getCoursesByQuery(@RequestParam("searchQuery") String searchQuery) {
        return new ResponseEntity<>(trainingCourseService.searchByQuery(searchQuery), HttpStatus.OK);
    }

    @Operation(
            summary = "Получить список курсов текущего пользователя",
            description = "Возвращает список курсов, созданных текущим пользователем. Доступ к этому методу ограничен авторизованными пользователями."
    )
    @GetMapping("/my-courses")
    public ResponseEntity<List<TrainingCourseLiteDTO>> getCoursesByAuthor(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok().body(trainingCourseService.getUserTrainingCourses(userDetails));
    }

    @Operation(
            summary = "Создать новый курс по бодибилдингу",
            description = "Создает новый курс по бодибилдингу с указанными данными, включая постер, трейлер и видеофайлы. Доступ к этому методу ограничен авторизованными пользователями."
    )
    @PreAuthorize("isAuthenticated()")
    @PostMapping()
    public ResponseEntity<HttpStatus> createTrainingCourse(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestPart("data") @Valid String data,
            @RequestPart(value = "poster", required = false) MultipartFile poster,
            @RequestPart(value = "trailer", required = false) MultipartFile trailer,
            @RequestPart(value = "files", required = false) MultipartFile[] files
    ) {

        trainingCourseService.createTrainingCourse(userDetails, data, poster, trailer, files);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(
            summary = "Обновить курс по бодибилдингу",
            description = "Обновляет существующий курс по бодибилдингу с указанными данными, включая постер, трейлер и видеофайлы. Доступ к этому методу ограничен автором курса, администраторами и модераторами."
    )
    @PreAuthorize("@coursePermissionService.isCourseAuthor(#courseId, principal.username)")
    @PutMapping("/update/{courseId}")
    public ResponseEntity<HttpStatus> updateTrainingCourse(
            @PathVariable int courseId,
            @RequestPart("data") String data,
            @RequestPart(value = "poster", required = false) MultipartFile poster,
            @RequestPart(value = "trailer", required = false) MultipartFile trailer,
            @RequestPart(value = "files", required = false) MultipartFile[] files
    ) {
        trainingCourseService.updateTrainingCourse(courseId, data, poster, trailer, files);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }

    @Operation(
            summary = "Удалить курс",
            description = "Удаляет курс по его идентификатору. Доступ к этому методу ограничен автором курса, администраторами и модераторами."
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR') or " +
            "@coursePermissionService.isCourseAuthor(#courseId, principal.username)")
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteTrainingCourse(@PathVariable("id") int courseId) {
        trainingCourseService.deleteTrainingCourseById(courseId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
