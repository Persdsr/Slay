package com.slay.support.service;

import com.slay.support.dto.request.support.SupportAcceptRequest;
import com.slay.support.dto.request.support.SupportMessageRequest;
import com.slay.support.dto.response.support.SupportRequestDTO;
import com.slay.support.entity.support.SupportRequestEntity;
import com.slay.support.entity.support.SupportRequestMessageEntity;
import com.slay.support.exception.NotFoundException;
import com.slay.support.exception.SupportResolvedException;
import com.slay.support.repository.SupportRequestMessageRepo;
import com.slay.support.repository.SupportRequestRepo;
import com.slay.support.service.file.FileService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SupportService {

    private final SupportRequestRepo supportRepo;
    private final SupportRequestMessageRepo supportRequestMessageRepo;
    private final FileService fileService;

    public List<SupportRequestDTO> getAllSupportRequests() {
        return supportRepo.findAll().stream()
                .map(SupportRequestDTO::toModel)
                .collect(Collectors.toList());
    }

    public List<SupportRequestDTO> getAllUserSupportRequests(int supportId) {
        return supportRepo.findAllBySender(supportId).stream()
                .map(SupportRequestDTO::toModel)
                .collect(Collectors.toList());
    }

    public SupportRequestDTO getSupportDetailById(int supportId) {
        return SupportRequestDTO.toModel(supportRepo.findById(supportId).orElseThrow(
                () -> NotFoundException.builder().build()
        ));
    }

    public void createSupportRequest(int userId, SupportAcceptRequest supportRequest, MultipartFile[] images) {
        SupportRequestEntity supportRequestEntity = new SupportRequestEntity();
        supportRequestEntity.setSender(userId);
        supportRequestEntity.setEmail(supportRequest.getEmail());
        supportRequestEntity.setRequestType(supportRequest.getRequestType());
        supportRequestEntity.setSubject(supportRequest.getSubject());

        SupportRequestMessageEntity supportRequestMessageEntity = new SupportRequestMessageEntity();
        if (images != null && images.length > 0) {
            supportRequestMessageEntity.setImages(fileService.saveAllFileSet(images));
        }

        supportRepo.save(supportRequestEntity);

        supportRequestMessageEntity.setSender(userId);
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

        supportRequestMessageEntity.setSender(supportMessageRequest.getSenderId());
        supportRequestMessageEntity.setMessage(supportMessageRequest.getMessage());

        SupportRequestEntity supportRequestEntity = supportRepo.findById(supportMessageRequest.getSupportRequestId()).orElseThrow(
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
