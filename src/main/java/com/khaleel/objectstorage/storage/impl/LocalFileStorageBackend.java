package com.khaleel.objectstorage.storage.impl;

import com.khaleel.objectstorage.storage.StorageBackend;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Component
public class LocalFileStorageBackend implements StorageBackend {
    private final Path rootLocation;

    public LocalFileStorageBackend(Path rootLocation) {
        this.rootLocation = rootLocation;
    }
    private void init(){
        try{
            Files.createDirectories(rootLocation);
        }
        catch (IOException e){
            throw new RuntimeException("Could not initialize the storage");
        }
    }

    @Override
    public void save(String bucketName, String physicalFileName, InputStream inputStream, long size) throws IOException{
        Path bucketDir = rootLocation.resolve(bucketName);
        if(!Files.exists(bucketDir)){
            Files.createDirectories(bucketDir);
        }
        Path destination = bucketDir.resolve(physicalFileName);
        Files.copy(inputStream, destination, StandardCopyOption.REPLACE_EXISTING);
    }

    @Override
    public InputStream load(String bucketName, String physicalFileName) throws IOException {
        Path file = rootLocation.resolve(bucketName).resolve(physicalFileName);
        if(!Files.exists(file)){
            throw new IOException("File not found " + physicalFileName);
        }
        return Files.newInputStream(file);
    }

    @Override
    public void delete(String bucketName, String physicalFileName) throws IOException {
        Path file = rootLocation.resolve(bucketName).resolve(physicalFileName);
        Files.deleteIfExists(file);
    }

    @Override
    public void createBucket(String bucketName) throws IOException {
        Path bucketDir = rootLocation.resolve(bucketName);
        if(!Files.exists(bucketDir)){
            Files.createDirectories(bucketDir);
        }
    }

    @Override
    public void deleteBucket(String bucketName) throws IOException {
        Path bucketDir = rootLocation.resolve(bucketName);
        Files.deleteIfExists(bucketDir);
    }
}
