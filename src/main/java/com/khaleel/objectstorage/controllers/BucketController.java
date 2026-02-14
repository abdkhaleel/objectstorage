package com.khaleel.objectstorage.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/buckets")
public class BucketController {
    //todo bucket service injection
    //private BucketService bucketService;

    @PostMapping
    public ResponseEntity<?> createBucket(@RequestParam String bucketName){
        return ResponseEntity.ok("Bucket created: " + bucketName);
    }

    @GetMapping
    public ResponseEntity<?> listBuckets(){
        return ResponseEntity.ok("List of buckets...");
    }

    @DeleteMapping("/{bucketName}")
    public ResponseEntity<?> deleteBucket(@PathVariable String bucketname){
        return ResponseEntity.ok("Deleting bucket");
    }
}
