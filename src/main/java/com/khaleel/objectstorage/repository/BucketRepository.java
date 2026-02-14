package com.khaleel.objectstorage.repository;

import com.khaleel.objectstorage.model.Bucket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BucketRepository extends JpaRepository<Bucket, Long> {
    Optional<Bucket> findByBucketName(String bucketName);
    List<Bucket> findByOwnerId(Long ownerId);
}
