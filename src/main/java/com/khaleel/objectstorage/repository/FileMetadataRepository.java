package com.khaleel.objectstorage.repository;

import com.khaleel.objectstorage.model.Bucket;
import com.khaleel.objectstorage.model.FileMetadata;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FileMetadataRepository extends JpaRepository<FileMetadata, Long> {
    Optional<FileMetadata> findByBucketAndFileName(Bucket bucket, String fileName);
    List<FileMetadata> findByBucket(Bucket bucket);
}
