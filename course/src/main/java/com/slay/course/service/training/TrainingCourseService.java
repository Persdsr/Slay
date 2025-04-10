package com.slay.course.service.training;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.slay.course.DTO.request.course.TrainingCourseRequest;
import com.slay.course.DTO.response.course.*;
import com.slay.course.DTO.response.user.UserLiteDTO;
import com.slay.course.entity.category.TagEntity;
import com.slay.course.entity.course.TrainingCourseEntity;
import com.slay.course.entity.course.TrainingCourseStepDetailEntity;
import com.slay.course.entity.course.TrainingCourseStepEntity;
import com.slay.course.entity.user.UserEntity;
import com.slay.course.exception.CategoryNotFoundException;
import com.slay.course.exception.InvalidJsonException;
import com.slay.course.exception.TrainingCourseNotFoundException;
import com.slay.course.exception.UserNotFoundException;
import com.slay.course.repository.category.CategoryRepo;
import com.slay.course.repository.course.TagRepo;
import com.slay.course.repository.course.TrainingCourseRepo;
import com.slay.course.repository.course.TrainingCourseStepDetailRepo;
import com.slay.course.repository.course.TrainingCourseStepRepo;
import com.slay.course.repository.user.ChatRepo;
import com.slay.course.repository.user.UserRepo;
import com.slay.course.service.StorageService;
import com.slay.course.service.file.FileService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class TrainingCourseService {

    private final TrainingCourseRepo trainingCourseRepo;
    private final UserRepo userRepo;
    private final StorageService storageService;
    private final CategoryRepo categoryRepo;
    private final ObjectMapper objectMapper;
    private final TrainingCourseStepRepo trainingCourseStepRepo;
    private final TrainingCourseStepDetailRepo trainingCourseStepDetailRepo;
    private final FileService fileService;
    private final TagRepo tagRepo;
    private final ChatRepo chatRepo;


    @Cacheable(value = "training-course-detail", key = "#courseId")
    public CourseResponse getTrainingCourseDetail(@AuthenticationPrincipal UserDetails userDetails, int courseId) {
        TrainingCourseEntity trainingCourse = trainingCourseRepo.findById(courseId).orElseThrow(
                () -> TrainingCourseNotFoundException.builder().build()
        );

        if (userDetails == null) {
            return TrainingCourseCroppedDTO.toModel(trainingCourse);
        }

        UserEntity user = userRepo.findByUsername(userDetails.getUsername()).orElseThrow(
                () -> UserNotFoundException.builder().build()
        );

        boolean isBuyer = trainingCourse.getCourseBuyers().stream()
                .anyMatch(buyer -> buyer.getId() == user.getId());

        if (isBuyer || trainingCourse.getAuthor().getUsername().equals(user.getUsername())) {
            TrainingCourseDetailDTO course = TrainingCourseDetailDTO.toModel(trainingCourse);
            return course;
        } else {
            return TrainingCourseCroppedDTO.toModel(trainingCourse);
        }
    }

    public SearchDTO searchByQuery(String searchQuery) {
        LevenshteinDistance distance = new LevenshteinDistance();

        List<TagCoursesDTO> tags = tagRepo.findByNameContainingIgnoreCase(searchQuery)
                .stream()
                .map(TagCoursesDTO::toModel)
                .collect(Collectors.toList());

        List<UserLiteDTO> authors = userRepo.findAll().stream()
                .filter(user -> {
                    String username = user.getUsername();
                    return username != null && distance.apply(username.toLowerCase(), searchQuery.toLowerCase()) <= 3;
                })
                .map(UserLiteDTO::toModel)
                .collect(Collectors.toList());

        List<CategoryWithTrainingCourseDTO> categories = categoryRepo.findAll().stream()
                .filter(category -> {
                    String categoryName = category.getName();
                    return categoryName != null && distance.apply(categoryName.toLowerCase(), searchQuery.toLowerCase()) <= 3;
                })
                .map(CategoryWithTrainingCourseDTO::toModel)
                .collect(Collectors.toList());

        List<TrainingCourseLiteDTO> courses = trainingCourseRepo.findAll().stream()
                .filter(course -> {
                    String courseName = course.getName();
                    return courseName != null && distance.apply(courseName.toLowerCase(), searchQuery.toLowerCase()) <= 4;
                })
                .map(TrainingCourseLiteDTO::toModel)
                .collect(Collectors.toList());

        return new SearchDTO(tags, categories, authors, courses);
    }

    @Cacheable(value = "user-training-courses", key = "#userDetails.username")
    public List<TrainingCourseLiteDTO> getUserTrainingCourses(UserDetails userDetails) {
        List<TrainingCourseEntity> courses = trainingCourseRepo.findAllByAuthorUsername(
                userDetails.getUsername()
        );
        return courses.stream().map(TrainingCourseLiteDTO::toModel).collect(Collectors.toList());
    }

    @Transactional
    @CacheEvict(value = "user-training-courses", key = "#userDetails.username")
    public void createTrainingCourse(
            UserDetails userDetails,
            String data,
            MultipartFile poster,
            MultipartFile trailer,
            MultipartFile[] files
    ) {
        TrainingCourseEntity trainingCourse = new TrainingCourseEntity();
        TrainingCourseRequest trainingCourseRequest = new TrainingCourseRequest();
        try {
            trainingCourseRequest = objectMapper.readValue(data, TrainingCourseRequest.class);
        } catch (IOException exception) {
            throw new InvalidJsonException("Invalid JSON " + exception.getMessage());
        }

        trainingCourse.setName(trainingCourseRequest.getName());
        trainingCourse.setDescription(trainingCourseRequest.getDescription());
        trainingCourse.setPrice(trainingCourseRequest.getPrice());
        trainingCourse.setAuthor(userRepo.findByUsername(userDetails.getUsername()).orElseThrow());

        trainingCourse.setCategory(categoryRepo.findByName(trainingCourseRequest.getCategory()).orElseThrow(
                () -> CategoryNotFoundException.builder().build()
        ));

        Set<String> tagNames = trainingCourseRequest.getTags();
        List<TagEntity> tags = tagRepo.findAllByNameIn(tagNames);
        List<String> existingTagNames = tags.stream().map(TagEntity::getName).collect(Collectors.toList());

        tagNames.stream()
                .filter(tagName -> !existingTagNames.contains(tagName))
                .forEach(tagName -> {
                    TagEntity newTag = new TagEntity();
                    newTag.setName(tagName);
                    tagRepo.save(newTag);
                    tags.add(newTag);
                });

        trainingCourse.setTags(tags);

        trainingCourse.setPoster(fileService.saveFile(poster));

        if (trailer != null) {
            trainingCourse.setTrailer(fileService.saveFile(trailer));
        }
        List<String> videoUrls = new ArrayList<>();
        if (files != null) {

            videoUrls = fileService.saveAllFileList(files);
        }

        int videoIndex = 0;

        for (TrainingCourseRequest.TrainingCourseStepDto stepDto : trainingCourseRequest.getTrainingCourseSteps()) {
            TrainingCourseStepEntity step = new TrainingCourseStepEntity();
            step.setTitle(stepDto.getTitle());
            step.setDescription(stepDto.getDescription());

            for (TrainingCourseRequest.TrainingCourseStepDetailDto detailDto : stepDto.getTrainingCourseStepDetails()) {
                TrainingCourseStepDetailEntity detail = new TrainingCourseStepDetailEntity();
                detail.setTitle(detailDto.getTitle());
                detail.setDescription(detailDto.getDescription());

                if (videoIndex < videoUrls.size()) {
                    detail.setVideo(videoUrls.get(videoIndex));
                    videoIndex++;
                }

                detail.setTrainingCourseStep(step);
                step.addDetail(detail);
            }

            trainingCourse.addStep(step);
        }

        trainingCourseRepo.save(trainingCourse);
    }


    @Transactional
    @CacheEvict(value = "user-training-courses", key = "#userDetails.username")
    public void updateTrainingCourse(
            int courseId,
            String data,
            MultipartFile poster,
            MultipartFile trailer,
            MultipartFile[] files
    )  {
        TrainingCourseEntity trainingCourse = trainingCourseRepo.findById(courseId).orElse(null);

        TrainingCourseRequest trainingCourseRequest = null;
        try {
            trainingCourseRequest = objectMapper.readValue(data, TrainingCourseRequest.class);
        } catch (JsonProcessingException e) {
            throw new InvalidJsonException("Invalid JSON " + e.getMessage());
        }

        trainingCourse.setName(trainingCourseRequest.getName());
        trainingCourse.setDescription(trainingCourseRequest.getDescription());
        trainingCourse.setPrice(trainingCourseRequest.getPrice());

        Set<String> tagNames = trainingCourseRequest.getTags();
        List<TagEntity> tags = tagRepo.findAllByNameIn(tagNames);
        List<String> existingTagNames = tags.stream().map(TagEntity::getName).collect(Collectors.toList());

        tagNames.stream()
                .filter(tagName -> !existingTagNames.contains(tagName))
                .forEach(tagName -> {
                    TagEntity newTag = new TagEntity();
                    newTag.setName(tagName);
                    tagRepo.save(newTag);
                    tags.add(newTag);
                });

        trainingCourse.setTags(tags);

        trainingCourse.setCategory(categoryRepo.findByName(trainingCourseRequest.getCategory()).orElseThrow(
                () -> CategoryNotFoundException.builder().build()
        ));

        List<String> videoUrls = new ArrayList<>();
        if (files != null) {
            videoUrls = fileService.saveAllFileList(files);
        }

        if (poster != null) {
            trainingCourse.setPoster(fileService.saveFile(poster));
        }
        if (trailer != null) {
            trainingCourse.setTrailer(fileService.saveFile(trailer));
        }

        trainingCourse.getTrainingCourseSteps().clear();
        int videoIndex = 0;

        for (TrainingCourseRequest.TrainingCourseStepDto stepDto : trainingCourseRequest.getTrainingCourseSteps()) {
            TrainingCourseStepEntity step = new TrainingCourseStepEntity();
            step.setTitle(stepDto.getTitle());
            step.setDescription(stepDto.getDescription());

            for (TrainingCourseRequest.TrainingCourseStepDetailDto detailDto : stepDto.getTrainingCourseStepDetails()) {
                TrainingCourseStepDetailEntity detail = new TrainingCourseStepDetailEntity();
                detail.setTitle(detailDto.getTitle());
                detail.setDescription(detailDto.getDescription());

                if (videoIndex < videoUrls.size()) {
                    detail.setVideo(videoUrls.get(videoIndex));
                    videoIndex++;
                } else if (detailDto.getVideo() != null) {
                    detail.setVideo(detailDto.getVideo());
                }

                detail.setTrainingCourseStep(step);
                step.addDetail(detail);
            }

            trainingCourse.addStep(step);
        }

        trainingCourseRepo.save(trainingCourse);
    }

    @Transactional
    public void handleBuyTrainingCourse(int trainingCourseId, String buyerUsername) {
        TrainingCourseEntity trainingCourse = trainingCourseRepo.findById(trainingCourseId)
                .orElseThrow(() ->
                    TrainingCourseNotFoundException.builder().build()
                );

        UserEntity buyer = userRepo.findByUsername(buyerUsername)
                .orElseThrow(() ->
                    UserNotFoundException.builder().build()
                );

        buyer.getPurchasedTrainingCourses().add(trainingCourse);
        trainingCourse.getCourseBuyers().add(buyer);

        userRepo.save(buyer);
        trainingCourseRepo.save(trainingCourse);

    }

    @CacheEvict(value = "user-training-courses", key = "#userDetails.username")
    public void deleteTrainingCourseById(int courseId) {
        TrainingCourseEntity trainingCourse = trainingCourseRepo.findById(courseId)
                .orElseThrow(() -> TrainingCourseNotFoundException.builder().build());

        trainingCourseRepo.deleteById(courseId);
    }

}
