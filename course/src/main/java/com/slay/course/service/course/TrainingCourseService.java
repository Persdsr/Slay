package com.slay.course.service.course;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.slay.course.dto.request.course.CourseCreateEvent;
import com.slay.course.dto.request.course.CoursePurchasedEvent;
import com.slay.course.dto.request.course.TrainingCourseRequest;
import com.slay.course.dto.response.course.*;

import com.slay.course.client.UserServiceClient;
import com.slay.course.entity.category.TagEntity;
import com.slay.course.entity.course.TrainingCourseEntity;
import com.slay.course.entity.course.TrainingCourseStepDetailEntity;
import com.slay.course.entity.course.TrainingCourseStepEntity;

import com.slay.course.exception.CategoryNotFoundException;
import com.slay.course.exception.InvalidJsonException;
import com.slay.course.exception.TrainingCourseNotFoundException;

import com.slay.course.security.UserDetailsImpl;
import com.slay.course.repository.category.CategoryRepo;
import com.slay.course.repository.course.TagRepo;
import com.slay.course.repository.course.TrainingCourseRepo;
import com.slay.course.repository.course.TrainingCourseStepDetailRepo;
import com.slay.course.repository.course.TrainingCourseStepRepo;

import com.slay.course.service.StorageService;
import com.slay.course.service.file.FileService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class TrainingCourseService {

    private final TrainingCourseRepo trainingCourseRepo;
    private final StorageService storageService;
    private final CategoryRepo categoryRepo;
    private final ObjectMapper objectMapper;
    private final TrainingCourseStepRepo trainingCourseStepRepo;
    private final TrainingCourseStepDetailRepo trainingCourseStepDetailRepo;
    private final FileService fileService;
    private final TagRepo tagRepo;
    private final UserServiceClient userServiceClient;
    private final KafkaTemplate<String, CoursePurchasedEvent> buyCourseKafkaTemplate;
    private final KafkaTemplate<String, CourseCreateEvent> createCourseKafkaTemplate;
    private final String TOPIC = "buy-course";


    @Cacheable(value = "course-course-detail", key = "#courseId")
    public CourseResponse getTrainingCourseDetail(UserDetailsImpl userDetails, int courseId) {
        TrainingCourseEntity trainingCourse = trainingCourseRepo.findById(courseId).orElseThrow(
                () -> TrainingCourseNotFoundException.builder().build()
        );

        if (userDetails == null) {
            TrainingCourseCroppedDTO trainingCourseCroppedDTO = TrainingCourseCroppedDTO.toModel(trainingCourse);
            try {
                trainingCourseCroppedDTO.setAuthor(userServiceClient.getUserLiteInfo(trainingCourse.getAuthorId()));
            } catch (Exception e) {
                trainingCourseCroppedDTO.setAuthor(null);
            }
            return trainingCourseCroppedDTO;
        }

        boolean isBuyer = trainingCourse.getBuyers().stream()
                .anyMatch(buyer -> buyer == userDetails.getId());

        if (isBuyer || trainingCourse.getAuthorId() == userDetails.getId()) {
            TrainingCourseDetailDTO course = TrainingCourseDetailDTO.toModel(trainingCourse);
            try {
                course.setAuthor(userServiceClient.getUserLiteInfo(trainingCourse.getAuthorId()));
            } catch (Exception e) {
                course.setAuthor(null);
            }
            return course;
        } else {
            TrainingCourseCroppedDTO trainingCourseCroppedDTO = TrainingCourseCroppedDTO.toModel(trainingCourse);
            try {
                trainingCourseCroppedDTO.setAuthor(userServiceClient.getUserLiteInfo(trainingCourse.getAuthorId()));
            } catch (Exception e) {
                trainingCourseCroppedDTO.setAuthor(null);
            }
            return trainingCourseCroppedDTO;
        }
    }

    @Cacheable(value = "user-course-courses", key = "#userDetails.username")
    public List<TrainingCourseLiteDTO> getUserTrainingCourses(UserDetailsImpl userDetails) {
        List<TrainingCourseEntity> courses = trainingCourseRepo.findAllByAuthorId(
                userDetails.getId()
        );
        return courses.stream().map(TrainingCourseLiteDTO::toModel).collect(Collectors.toList());
    }

    public List<TrainingCourseLiteDTO> getCoursesByIds(List<Integer> courseIds) {
        List<TrainingCourseEntity> courses = trainingCourseRepo.findAllById(courseIds);
        return courses.stream()
                .map(TrainingCourseLiteDTO::toModel)
                .collect(Collectors.toList());
    }

    public SearchDTO searchByQuery(String searchQuery) {
        LevenshteinDistance distance = new LevenshteinDistance();

        List<TagCoursesDTO> tags = tagRepo.findByNameContainingIgnoreCase(searchQuery)
                .stream()
                .map(TagCoursesDTO::toModel)
                .collect(Collectors.toList());

        /*List<UserLiteDTO> authors = userRepo.findAll().stream()
                .filter(user -> {
                    String username = user.getUsername();
                    return username != null && distance.apply(username.toLowerCase(), searchQuery.toLowerCase()) <= 3;
                })
                .map(UserLiteDTO::toModel)
                .collect(Collectors.toList());*/

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

        return new SearchDTO(tags, categories, List.of(), courses);
    }

    public List<TrainingCourseLiteDTO> getTrainingCoursesByAuthorId(Integer authorId) {
        List<TrainingCourseEntity> courses = trainingCourseRepo.findAllByAuthorId(
                authorId
        );
        return courses.stream().map(TrainingCourseLiteDTO::toModel).collect(Collectors.toList());
    }

    @Transactional
    @CacheEvict(value = "user-course-courses", key = "#userDetails.username")
    public void createTrainingCourse(
            UserDetailsImpl userDetails,
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
        trainingCourse.setAuthorId(userDetails.getId());

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
        createCourseKafkaTemplate.send("create-course", CourseCreateEvent.builder()
                .courseId(trainingCourse.getId())
                .authorId(trainingCourse.getAuthorId())
                .build());
    }


    @Transactional
    @CacheEvict(value = "user-training-courses", key = "#userDetails.username")
    public void updateTrainingCourse(
            int courseId,
            String data,
            MultipartFile poster,
            MultipartFile trailer,
            MultipartFile[] files
    ) {
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
    public void handleBuyTrainingCourse(int trainingCourseId, Integer buyerId) {
        TrainingCourseEntity trainingCourse = trainingCourseRepo.findById(trainingCourseId)
                .orElseThrow(() -> TrainingCourseNotFoundException.builder().build());

        trainingCourse.getBuyers().add(buyerId);
        trainingCourseRepo.save(trainingCourse);

        CoursePurchasedEvent event = new CoursePurchasedEvent(buyerId, trainingCourseId, LocalDateTime.now());
        buyCourseKafkaTemplate.send(TOPIC, event);
    }

    @CacheEvict(value = "user-course-courses", key = "#userDetails.username")
    public void deleteTrainingCourseById(int courseId) {
        TrainingCourseEntity trainingCourse = trainingCourseRepo.findById(courseId)
                .orElseThrow(() -> TrainingCourseNotFoundException.builder().build());

        trainingCourseRepo.deleteById(courseId);
    }

}
