package com.khaleel.objectstorage.controller;

import com.khaleel.objectstorage.model.Bucket;
import com.khaleel.objectstorage.security.SecurityUser;
import com.khaleel.objectstorage.service.BucketService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/buckets")
public class BucketController {

    private final BucketService bucketService;

    public BucketController(BucketService bucketService) {
        this.bucketService = bucketService;
    }

    @PostMapping
    public ResponseEntity<Bucket> createBucket(@RequestParam String bucketName, @AuthenticationPrincipal SecurityUser user) {
        Long userId = user.getId();

        Bucket bucket = bucketService.createBucket(bucketName, userId);
        return ResponseEntity.ok(bucket);
    }

    @GetMapping
    public ResponseEntity<List<Bucket>> listBuckets(@AuthenticationPrincipal SecurityUser user) {
        Long userId = user.getId();
        return ResponseEntity.ok(bucketService.getAllBuckets(userId));
    }

    @DeleteMapping("/{bucketName}")
    public ResponseEntity<String> deleteBucket(@PathVariable String bucketName, @AuthenticationPrincipal SecurityUser user) {
        Long userId = user.getId();
        bucketService.deleteBucket(bucketName, userId);
        return ResponseEntity.ok("Bucket '" + bucketName + "' deleted successfully");
    }
}