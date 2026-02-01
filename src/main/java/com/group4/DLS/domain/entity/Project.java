package com.group4.DLS.domain.entity;

import jakarta.persistence.GeneratedValue;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.group4.DLS.domain.entity.enums.ProjectStatus;

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
    String projectId;

    @Column(nullable = false, unique = true)
    String projectName;

    String description;

    @Enumerated(EnumType.STRING)
    ProjectStatus status;

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

    // One project has Many Assignment
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Assignment> assignments = new ArrayList<>();

    // One project has Many Guideline
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Guideline> guidelines = new ArrayList<>();

    // One project has Many Dataset
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Dataset> datasets = new ArrayList<>();

    // One project has Many Dataset
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Dataitem> dataitems = new ArrayList<>();

    // One project has Many Label Schema
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<LabelSchema> schemas = new ArrayList<>();
}
