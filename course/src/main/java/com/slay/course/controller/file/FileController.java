package com.slay.course.controller.file;

import com.slay.course.service.StorageService;
import com.slay.course.service.file.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@AllArgsConstructor
@Tag(name = "File", description = "Загрузка и просмотр файлов")
public class FileController {

    private final StorageService storageService;
    private final FileService fileService;

    @Operation(
            summary = "Загрузить файл",
            description = "Позволяет загрузить файл по его имени. Файл возвращается в виде ресурса с заголовком `Content-Disposition`, который указывает браузеру скачать файл."
    )
    @GetMapping("/download/{filename:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename) {
        Resource resource = storageService.loadAsResource(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @Operation(
            summary = "Просмотреть файл",
            description = "Позволяет просмотреть файл (например, видео) непосредственно в браузере. Файл возвращается в виде ресурса с заголовком `Content-Disposition`, который указывает браузеру отобразить файл, а не скачать его."
    )
    @GetMapping("/view/{filename:.+}")
    public ResponseEntity<Resource> seeFile(@PathVariable String filename) {
        Resource resource = storageService.loadAsResource(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                .header(HttpHeaders.CONTENT_TYPE, "video/mp4")
                .body(resource);
    }

    @Operation(
            summary = "Загрузить несколько файлов",
            description = "Позволяет загрузить несколько файлов на сервер. После успешной загрузки возвращает список URL-адресов загруженных файлов."
    )
    @PostMapping("/api/files/upload")
    public ResponseEntity<List<String>> uploadFiles(@RequestParam("files") MultipartFile[] files) {
        List<String> fileUrls = fileService.saveAllFileList(files);

        return ResponseEntity.ok(fileUrls);
    }

    /*@PostMapping("/upload-multiple-files")
    public List<FileResponse> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {
        return Arrays.stream(files)
                .map(file -> uploadFile(file))
                .collect(Collectors.toList());
    }*/
}
