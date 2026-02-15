package com.khaleel.objectstorage.service.impl;

import com.khaleel.objectstorage.ObjectstorageApplication;
import com.khaleel.objectstorage.exception.*;
import com.khaleel.objectstorage.model.Bucket;
import com.khaleel.objectstorage.model.User;
import com.khaleel.objectstorage.repository.BucketRepository;
import com.khaleel.objectstorage.repository.UserRepository;
import com.khaleel.objectstorage.service.BucketService;
import com.khaleel.objectstorage.storage.StorageBackend;
import com.khaleel.objectstorage.storage.impl.LocalFileStorageBackend;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Service
public class BucketServiceImpl implements BucketService {
    private final BucketRepository bucketRepository;
    private final UserRepository userRepository;
    private final StorageBackend storageBackend;

    public BucketServiceImpl(BucketRepository bucketRepository, UserRepository userRepository, StorageBackend storageBackend) {
        this.bucketRepository = bucketRepository;
        this.userRepository = userRepository;
        this.storageBackend = storageBackend;
    }

    @Override
    @Transactional
    public Bucket createBucket(String bucketName, Long userId) {
        if(bucketRepository.findByBucketName(bucketName).isPresent()){
            throw new BucketAlreadyExistsException("Bucket name already taken");
        }

        User owner = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User does not  exists"));

        try{
            storageBackend.createBucket(bucketName);
        }
        catch (IOException e){
            throw new StorageException("Failed to create bucket", e);
        }

        Bucket bucket = Bucket.builder()
                .bucketName(bucketName)
                .owner(owner)
                .build();

        return bucketRepository.save(bucket);
    }

    @Override
    public List<Bucket> getAllBuckets(Long userId) {
        return bucketRepository.findByOwnerId(userId);
    }

    @Override
    @Transactional
    public void deleteBucket(String bucketName, Long userId) {
        Bucket bucket = bucketRepository.findByBucketName(bucketName).orElseThrow(() -> new BucketNotFoundException("Bucket does not exists..."));

        if(!bucket.getOwner().getId().equals(userId)){
            throw new UnAuthorizedUserException("Access Denied; you don't own this bucket");
        }
        try{
            storageBackend.deleteBucket(bucketName);
        }
        catch (IOException e){
            throw new StorageException("Failed to delete bucket", e);
        }
        bucketRepository.delete(bucket);
    }
}
