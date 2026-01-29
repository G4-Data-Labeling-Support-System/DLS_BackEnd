package com.group4.DLS.domain.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "dataitems")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class Dataitem {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.UUID)
    String dataitemId;

    @Column(nullable = false, unique = true)
    String dataitemName;

    @Column(nullable = true)
    String dataType;

    @Column(nullable = false)
    String uri;

    LocalDate createdAt;

    LocalDate updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDate.now();
        this.updatedAt = LocalDate.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDate.now();
    }

    // One Profile belongs to One User
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "taskId", unique = true)  // Foreign Key with unique constraint
    private Task task;

    // Many Dataitem belongs to One Project
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "projectId", nullable = false)
    private Project project;

    // Many Dataitem belongs to One Dataset
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "datasetId", nullable = false)
    private Dataset dataset;
}
