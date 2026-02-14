package com.khaleel.objectstorage.service;

import com.khaleel.objectstorage.model.Bucket;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;

public interface BucketService {
    Bucket createBucket(String bucketName, Long userId);
    List<Bucket> getAllBuckets(Long userId);
    void deleteBucket(String bucketName, Long userId);
}
