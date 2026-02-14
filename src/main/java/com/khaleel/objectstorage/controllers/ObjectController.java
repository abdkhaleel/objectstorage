package com.khaleel.objectstorage.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/buckets/{bucketName}/files")
public class ObjectController {

    //todo inject objectservice
    //private final ObjectService objectService;

    @PostMapping
    public ResponseEntity<?> uploadFile(@PathVariable String bucketName, @RequestParam("file") MultipartFile file){
        return ResponseEntity.ok("File uploaded successfully");
    }

    @GetMapping("/{fileName}")
    public ResponseEntity<?> downloadFile(@PathVariable String bucketName, @PathVariable String fileName){
        return ResponseEntity.ok("Downloading file");
    }

    @GetMapping
    public ResponseEntity<?> listFiles(@PathVariable String bucketName){
        return ResponseEntity.ok("List of files in " + bucketName);
    }

    @DeleteMapping("/{fileName}")
    public ResponseEntity<?> deleteFile(@PathVariable String bucketName, @PathVariable String fileName){
        return ResponseEntity.ok("Deleting file...");
    }
}
