package com.slay.course.service.user;

import com.slay.course.DTO.request.support.SupportAcceptRequest;
import com.slay.course.DTO.request.support.SupportMessageRequest;
import com.slay.course.DTO.response.support.SupportRequestDTO;
import com.slay.course.entity.support.SupportRequestEntity;
import com.slay.course.entity.support.SupportRequestMessageEntity;
import com.slay.course.entity.user.UserEntity;
import com.slay.course.exception.NotFoundException;
import com.slay.course.exception.SupportResolvedException;
import com.slay.course.exception.UserNotFoundException;
import com.slay.course.repository.support.SupportRequestMessageRepo;
import com.slay.course.repository.support.SupportRequestRepo;
import com.slay.course.repository.user.UserRepo;
import com.slay.course.service.file.FileService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SupportService {

    private final SupportRequestRepo supportRepo;
    private final UserRepo userRepo;
    private final SupportRequestRepo supportRequestRepo;
    private final SupportRequestMessageRepo supportRequestMessageRepo;
    private final FileService fileService;

    public List<SupportRequestDTO> getAllSupportRequests() {
        return supportRepo.findAll().stream()
                .map(SupportRequestDTO::toModel)
                .collect(Collectors.toList());
    }

    public List<SupportRequestDTO> getAllUserSupportRequests(String username) {
        return supportRepo.findAllBySenderUsername(username).stream()
                .map(SupportRequestDTO::toModel)
                .collect(Collectors.toList());
    }

    public SupportRequestDTO getSupportDetailById(int supportId) {
        return SupportRequestDTO.toModel(supportRequestRepo.findById(supportId).orElseThrow(
                () -> NotFoundException.builder().build()
        ));
    }

    public void createSupportRequest(UserDetails userDetails, SupportAcceptRequest supportRequest, MultipartFile[] images) {
        SupportRequestEntity supportRequestEntity = new SupportRequestEntity();
        supportRequestEntity.setSender(userRepo.findByUsername(userDetails.getUsername()).orElseThrow(
                () -> UserNotFoundException.builder().build()
        ));
        supportRequestEntity.setEmail(supportRequest.getEmail());
        supportRequestEntity.setRequestType(supportRequest.getRequestType());
        supportRequestEntity.setSubject(supportRequest.getSubject());

        SupportRequestMessageEntity supportRequestMessageEntity = new SupportRequestMessageEntity();
        if (images != null && images.length > 0) {
            supportRequestMessageEntity.setImages(fileService.saveAllFileSet(images));
        }

        supportRepo.save(supportRequestEntity);

        supportRequestMessageEntity.setSender(userRepo.findByUsername(userDetails.getUsername()).orElseThrow(
                () -> UserNotFoundException.builder().build()
        ));
        supportRequestMessageEntity.setMessage(supportRequest.getDescription());
        supportRequestMessageEntity.setSupportRequest(supportRequestEntity);

        supportRequestMessageRepo.save(supportRequestMessageEntity);
    }

    public void deleteSupport(int supportId) {
        supportRepo.findById(supportId).orElseThrow(
                () -> NotFoundException.builder().build()
        );

        supportRepo.deleteById(supportId);
    }

    @Transactional
    public void createSupportMessageWS(SupportMessageRequest supportMessageRequest) {
        SupportRequestMessageEntity supportRequestMessageEntity = new SupportRequestMessageEntity();
        UserEntity senderEntity = userRepo.findByUsername(supportMessageRequest.getSender().getUsername()).orElseThrow(
                () -> UserNotFoundException.builder().build()
        );

        supportRequestMessageEntity.setSender(senderEntity);
        supportRequestMessageEntity.setMessage(supportMessageRequest.getMessage());

        SupportRequestEntity supportRequestEntity = supportRequestRepo.findById(supportMessageRequest.getSupportRequestId()).orElseThrow(
                () -> NotFoundException.builder().build()
        );

        if (supportRequestEntity.isResolved()) {
            throw SupportResolvedException.builder().build();
        }

        supportRequestMessageEntity.setSupportRequest(supportRequestEntity);


        if (supportMessageRequest.getImages() != null && !supportMessageRequest.getImages().isEmpty()) {
            supportRequestMessageEntity.setImages(supportMessageRequest.getImages());
        }

        supportRequestMessageRepo.save(supportRequestMessageEntity);
    }

    public void changeSupportResolvedStatus(int supportId) {
        SupportRequestEntity supportRequestEntity = supportRepo.findById(supportId).orElse(null);
        supportRequestEntity.setResolved(!supportRequestEntity.isResolved());

        supportRepo.save(supportRequestEntity);
    }
}
