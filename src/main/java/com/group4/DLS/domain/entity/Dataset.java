package com.group4.DLS.domain.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "datasets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class Dataset {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "dataset_id")
    String datasetId;

    @Column(name = "dataset_name", nullable = false)
    String datasetName;

    @Column(name = "description")
    String description;

    @Column(name = "total_items")
    int totalItems;

    @Column(name = "created_at")
    LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // Many Dataset belongs to One Prject
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = true)
    private Project project;
    
    // One Dataset has Many Dataitem
    @OneToMany(mappedBy = "dataset", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Dataitem> dataitems = new ArrayList<>();


    // One Dataset has One Assignment
    @OneToOne
    @JoinColumn(name = "assignment_id", nullable = true)
    private Assignment assignment;

    // One Dataset has Many Labels
    @OneToMany(mappedBy = "dataset", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Label> labels = new ArrayList<>();

}
