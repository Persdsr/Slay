package com.slay.course.controller.course;

import com.slay.course.dto.response.course.CourseResponse;
import com.slay.course.dto.response.course.SearchDTO;
import com.slay.course.dto.response.course.TrainingCourseLiteDTO;
import com.slay.course.security.UserDetailsImpl;
import com.slay.course.service.course.TrainingCourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public ResponseEntity<CourseResponse> getTrainingCourseDetail(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable("course-id") int courseId) {
        return new ResponseEntity<>(trainingCourseService.getTrainingCourseDetail(userDetails, courseId), HttpStatus.OK);
    }

    @Operation(
            summary = "Получить список курсов текущего пользователя",
            description = "Возвращает список курсов, созданных текущим пользователем. Доступ к этому методу ограничен авторизованными пользователями."
    )
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/my-courses")
    public ResponseEntity<List<TrainingCourseLiteDTO>> getCoursesByAuthorId(@AuthenticationPrincipal UserDetailsImpl userDetails) {

        return ResponseEntity.ok().body(trainingCourseService.getUserTrainingCourses(userDetails));
    }

    @GetMapping("/courses-by-ids")
    public ResponseEntity<List<TrainingCourseLiteDTO>> getCoursesByIds(@RequestParam("ids") List<Integer> ids) {

        return ResponseEntity.ok().body(trainingCourseService.getCoursesByIds(ids));
    }


    @PostMapping("/buy")
    @PreAuthorize("isAuthenticated()")
    public String buyTest(@RequestParam("courseId") Integer courseId,
                          @AuthenticationPrincipal UserDetailsImpl userDetails) {
        trainingCourseService.handleBuyTrainingCourse(courseId, userDetails.getId());
        return "OK";
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
    @GetMapping("/author")
    public ResponseEntity<List<TrainingCourseLiteDTO>> getTrainingCoursesByAuthorId(@RequestParam("authorId") Integer authorId) {
        return ResponseEntity.ok().body(trainingCourseService.getTrainingCoursesByAuthorId(authorId));
    }

    @Operation(
            summary = "Создать новый курс",
            description = "Создает новый курс с указанными данными, включая постер, трейлер и видеофайлы. Доступ к этому методу ограничен авторизованными пользователями."
    )
    @PreAuthorize("isAuthenticated()")
    @PostMapping()
    public ResponseEntity<HttpStatus> createTrainingCourse(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestPart("data") @Valid String data,
            @RequestPart(value = "poster", required = false) MultipartFile poster,
            @RequestPart(value = "trailer", required = false) MultipartFile trailer,
            @RequestPart(value = "files", required = false) MultipartFile[] files
    ) {

        trainingCourseService.createTrainingCourse(userDetails, data, poster, trailer, files);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(
            summary = "Обновить курс",
            description = "Обновляет существующий курс указанными данными, включая постер, трейлер и видеофайлы. Доступ к этому методу ограничен автором курса, администраторами и модераторами."
    )
    @PreAuthorize("@coursePermissionService.isCourseAuthorById(#courseId, #userDetails.id)")
    @PutMapping("/update/{courseId}")
    public ResponseEntity<HttpStatus> updateTrainingCourse(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
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
            "#userDetails != null and @coursePermissionService.isCourseAuthorById(#courseId, #userDetails.id)")
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteTrainingCourse(@PathVariable("id") int courseId,
                                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
        trainingCourseService.deleteTrainingCourseById(courseId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
