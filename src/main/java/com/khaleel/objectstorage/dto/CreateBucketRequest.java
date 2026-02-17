package com.khaleel.objectstorage.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class CreateBucketRequest {
    @NotBlank(message = "Bucket name is required")
    @Pattern(regexp = "^[a-z0-9-]{3, 63}$", message = "Bucket name must be lowercase, 3-63 chars, numbers and hyphens only")
    private String bucketName;
}
