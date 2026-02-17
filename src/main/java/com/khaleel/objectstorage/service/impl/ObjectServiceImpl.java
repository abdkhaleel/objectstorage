package com.khaleel.objectstorage.service.impl;

import com.khaleel.objectstorage.exception.BucketNotFoundException;
import com.khaleel.objectstorage.exception.ObjectAlreadyExistsException;
import com.khaleel.objectstorage.exception.ObjectNotfoundException;
import com.khaleel.objectstorage.exception.UnAuthorizedUserException;
import com.khaleel.objectstorage.model.Bucket;
import com.khaleel.objectstorage.model.FileMetadata;
import com.khaleel.objectstorage.repository.BucketRepository;
import com.khaleel.objectstorage.repository.FileMetadataRepository;
import com.khaleel.objectstorage.repository.UserRepository;
import com.khaleel.objectstorage.service.ObjectService;
import com.khaleel.objectstorage.storage.StorageBackend;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ObjectServiceImpl implements ObjectService {
    private final FileMetadataRepository fileMetadataRepository;
    private final StorageBackend storageBackend;
    private final BucketRepository bucketRepository;

    public ObjectServiceImpl(FileMetadataRepository fileMetadataRepository, StorageBackend storageBackend, BucketRepository bucketRepository) {
        this.fileMetadataRepository = fileMetadataRepository;
        this.storageBackend = storageBackend;
        this.bucketRepository = bucketRepository;
    }

    @Override
    @Transactional
    public FileMetadata uploadFile(String bucketName, MultipartFile file, Long userId) throws IOException  {

        System.out.println("Calling upload file");

        Bucket bucket = bucketRepository.findByBucketName(bucketName).orElseThrow(() -> new BucketNotFoundException("Bucket does not exists.."));

        System.out.println("Ran query..");
        System.out.println("Checking for valid user");
        if(!bucket.getOwner().getId().equals(userId)){
            throw new UnAuthorizedUserException("Access Denied: You do not own this bucket");
        }
        System.out.println("Checking for duplicate file");
        Optional<FileMetadata> existingFile = fileMetadataRepository.findByBucketAndFileName(bucket, file.getOriginalFilename());
        if(existingFile.isPresent()){
//            throw new ObjectAlreadyExistsException("File with this name already inside this bucket");
            System.out.println("File already exist..\n REPLACING THE CONTENT..");
            FileMetadata oldMetadata = existingFile.get();

            //deleting old file from disk
            storageBackend.delete(bucketName, oldMetadata.getPhysicalName());

            //update
            String newPhysicalName = UUID.randomUUID().toString();
            storageBackend.save(bucketName, newPhysicalName, file.getInputStream(), file.getSize());

            oldMetadata.setPhysicalName(newPhysicalName);
            oldMetadata.setSize(file.getSize());
            oldMetadata.setContentType(file.getContentType());
            oldMetadata.setUploadedAt(LocalDateTime.now());

            return fileMetadataRepository.save(oldMetadata);
        }
        System.out.println("Generating physical name");
        String physicalName = UUID.randomUUID().toString();

        try {
            System.out.println("Trying to store in file system");
            storageBackend.save(bucketName, physicalName, file.getInputStream(), file.getSize());
            System.out.println("Stored Successfully");
        } catch (IOException e) {
            throw new RuntimeException("Upload failed " + e);
        }
        System.out.println("Storing meta data");
        FileMetadata fileMetadata = FileMetadata.builder()
                .bucket(bucket)
                .fileName(file.getOriginalFilename())
                .physicalName(physicalName)
                .size(file.getSize())
                .contentType(file.getContentType())
                .build();

        return fileMetadataRepository.save(fileMetadata);

    }

    @Override
    public Resource downloadFile(String bucketName, String fileName, Long userId) {
        Bucket bucket = bucketRepository.findByBucketName(bucketName).orElseThrow(() -> new BucketNotFoundException("Bucket does not exists"));

        FileMetadata fileMetadata = fileMetadataRepository.findByBucketAndFileName(bucket, fileName).orElseThrow(() -> new ObjectNotfoundException("File does not exists in this bucket"));

        if(!bucket.getOwner().getId().equals(userId)){
            throw new UnAuthorizedUserException("Access denied: you do not own this bucket");
        }

        try{
            InputStream inputStream = storageBackend.load(bucketName, fileMetadata.getPhysicalName());
            return new InputStreamResource(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Could not load from dist ", e);
        }
    }

    @Override
    public List<FileMetadata> getAllFiles(String bucketName, Long userId) {
        Bucket bucket = bucketRepository.findByBucketName(bucketName).orElseThrow(() -> new BucketNotFoundException("Bucket does not exists"));

        if(!bucket.getOwner().getId().equals(userId)){
            throw new UnAuthorizedUserException("Access denied: you do not own this bucket");
        }
        return fileMetadataRepository.findByBucket(bucket);
    }

    @Override
    @Transactional
    public void deleteFile(String bucketName, String fileName, Long userId) {
        Bucket bucket = bucketRepository.findByBucketName(bucketName).orElseThrow(() -> new BucketNotFoundException("Bucket does not exists"));

        FileMetadata fileMetadata = fileMetadataRepository.findByBucketAndFileName(bucket, fileName).orElseThrow(() -> new ObjectNotfoundException("File does not exists in this bucket"));

        if(!bucket.getOwner().getId().equals(userId)){
            throw new UnAuthorizedUserException("Access denied: you do not own this bucket");
        }
        fileMetadataRepository.delete(fileMetadata);
        try{
            storageBackend.delete(bucketName, fileMetadata.getPhysicalName());
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete file", e);
        }
        fileMetadataRepository.delete(fileMetadata);
    }


}
