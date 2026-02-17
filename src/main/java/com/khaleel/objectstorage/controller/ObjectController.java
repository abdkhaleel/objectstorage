package com.khaleel.objectstorage.controller;

import com.khaleel.objectstorage.model.FileMetadata;
import com.khaleel.objectstorage.security.SecurityUser;
import com.khaleel.objectstorage.service.ObjectService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/buckets/{bucketName}/files")
public class ObjectController {

    private final ObjectService objectService;

    public ObjectController(ObjectService objectService) {
        this.objectService = objectService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<FileMetadata> uploadFile(@PathVariable String bucketName,
                                                   @RequestParam("file") MultipartFile file,
                                                   @AuthenticationPrincipal SecurityUser user) throws IOException {
        Long userId = user.getId();
        FileMetadata metadata = objectService.uploadFile(bucketName, file, userId);
        return ResponseEntity.ok(metadata);
    }

    @GetMapping("/{fileName}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String bucketName,
                                                 @PathVariable String fileName,
                                                 @AuthenticationPrincipal SecurityUser user) {
        Long userId = user.getId();
        Resource resource = objectService.downloadFile(bucketName, fileName, userId);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .body(resource);
    }

    @GetMapping
    public ResponseEntity<List<FileMetadata>> listFiles(@PathVariable String bucketName,
                                                        @AuthenticationPrincipal SecurityUser user) {
        Long userId = user.getId();
        return ResponseEntity.ok(objectService.getAllFiles(bucketName, userId));
    }

    @DeleteMapping("/{fileName}")
    public ResponseEntity<String> deleteFile(@PathVariable String bucketName,
                                             @PathVariable String fileName,
                                             @AuthenticationPrincipal SecurityUser user) {
        Long userId = user.getId();
        objectService.deleteFile(bucketName, fileName, userId);
        return ResponseEntity.ok("File deleted successfully");
    }
}