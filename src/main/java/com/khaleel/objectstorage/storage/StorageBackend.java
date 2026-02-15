package com.khaleel.objectstorage.storage;

import java.io.IOException;
import java.io.InputStream;

public interface StorageBackend {
    void save(String bucketName, String physicalFileName, InputStream inputStream, long size) throws IOException;
    InputStream load(String bucketName, String physicalFileName) throws IOException;
    void delete(String bucketName, String physicalFileName) throws IOException;
    void createBucket(String bucketName) throws IOException;
    void deleteBucket(String bucketName) throws IOException;
}
