package com.khaleel.objectstorage.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "file_metadata", uniqueConstraints = {@UniqueConstraint(columnNames = {"bucket_id", "file_name"})})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileMetadata {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    private String physicalName;

    private String contentType;
    private Long size;

    private LocalDateTime uploadedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bucket_id", nullable = false)
    @JsonIgnore
    @ToString.Exclude
    private Bucket bucket;

    @PrePersist
    public void onCreate(){
        this.uploadedAt = LocalDateTime.now();
    }

}
