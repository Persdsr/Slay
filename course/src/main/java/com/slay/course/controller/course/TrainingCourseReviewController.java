package com.slay.course.controller.course;

import com.slay.course.DTO.request.course.ReviewRequest;
import com.slay.course.service.training.TrainingCourseReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Controller
@RequestMapping("/api/training-course/review")
@AllArgsConstructor
@Tag(name = "Course review", description = "Отзывы на курс")
public class TrainingCourseReviewController {

    private final TrainingCourseReviewService trainingCourseReviewService;

    @Operation(
            summary = "Создать отзыв о курсе",
            description = "Позволяется создать отзыв о курсе. Доступно только для аутентифицированным пользователям"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Отзыв успешно создан"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные в запросе"),
            @ApiResponse(responseCode = "401", description = "Пользователь не аутентифицирован"),
            @ApiResponse(responseCode = "404", description = "Курс с указанным ID не найден"),
            @ApiResponse(responseCode = "415", description = "Unsupported Media Type", content = @Content(
                    examples = @ExampleObject(value = "{\n" +
                            "  \"timestamp\": \"2025-03-12T13:39:23.322+00:00\",\n" +
                            "  \"status\": 415,\n" +
                            "  \"error\": \"Unsupported Media Type\",\n" +
                            "  \"path\": \"/api/training-course/review\"\n" +
                            "}")
            ))
    })
    @PreAuthorize("@coursePermissionService.isCourseBuyer(#trainingCourseId, principal.username)")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<HttpStatus> postTrainingCourseReview(
            @AuthenticationPrincipal UserDetails userDetails,
            @Parameter(
                    description = "Данные отзыва",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
            )
            @RequestPart("reviewFields") @Valid ReviewRequest review,

            @Parameter(description = "ID курса", required = true)
            @RequestParam("trainingCourseId") int trainingCourseId,

            @Parameter(description = "Изображения отзыва")
            @RequestPart(value = "images", required = false) MultipartFile[] images
    ) {
        trainingCourseReviewService.createReview(userDetails, review, trainingCourseId, images);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(
            summary = "Обновить поля отзыва",
            description = "Позволяет обновить поля отзыва по его идентификатору. Доступно только для аутентифицированных пользователей."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Отзыв успешно обновлен"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные в запросе"),
            @ApiResponse(responseCode = "401", description = "Пользователь не аутентифицирован"),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав"),
            @ApiResponse(responseCode = "404", description = "Отзыв с указанным ID не найден")
    })
    @PreAuthorize("@reviewPermissionService.isReviewAuthor(#reviewId, principal.username)")
    @PatchMapping("/update-fields/{id}")
    public ResponseEntity<HttpStatus> updateReviewFields(@RequestBody Map<String, Object> fields,
                                                      @PathVariable("id") int reviewId) {

        trainingCourseReviewService.updateReviewFields(reviewId, fields);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(
            summary = "Удалить отзыв",
            description = "Позволяет удалить отзыв по его идентификатору. Доступно только для пользователей с ролью ADMIN или MODERATOR."
    )
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR') or" +
            " @reviewPermissionService.isReviewAuthor(#reviewId, principal.username)")
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteReview(@PathVariable("id") int reviewId) {
        trainingCourseReviewService.deleteReviewById(reviewId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
