package com.khaleel.objectstorage.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "buckets")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Bucket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String bucketName;

    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    @JsonIgnore
    @ToString.Exclude
    private User owner;

    @OneToMany(mappedBy = "bucket", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<FileMetadata> files;

    @PrePersist
    public void onCreate(){
        this.createdAt = LocalDateTime.now();
    }
}