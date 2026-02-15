package com.khaleel.objectstorage.service;

import com.khaleel.objectstorage.model.FileMetadata;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ObjectService {
    FileMetadata uploadFile(String bucketName, MultipartFile file, Long userId) throws IOException;
    Resource downloadFile(String bucketName, String fileName, Long userId);
    List<FileMetadata> getAllFiles(String bucketName, Long userId);
    void deleteFile(String bucketName, String fileName, Long userId);
}
