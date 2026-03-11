package com.group4.DLS.domain.entity;

import jakarta.persistence.GeneratedValue;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.group4.DLS.domain.enums.ProjectStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "projects")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class Project {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.UUID)
    @Column(name = "project_id", nullable = false)
    String projectId;

    @Column(name = "project_name", nullable = false, unique = true)
    String projectName;

    @Column(name = "description", nullable = true)
    String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "project_status", nullable = false)
    ProjectStatus status = ProjectStatus.NOT_STARTED;

    @Column(name = "created_at")
    LocalDateTime createdAt;

    @Column(name = "updated_at")
    LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();

        if (status == null) {
            this.status = ProjectStatus.NOT_STARTED;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // One project has Many Assignment
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Assignment> assignments = new ArrayList<>();

    // One project has Many Guideline
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Guideline> guidelines = new ArrayList<>();

    // One project has Many Dataset
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Dataset> datasets = new ArrayList<>();
}
