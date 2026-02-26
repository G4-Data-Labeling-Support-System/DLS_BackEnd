package com.group4.DLS.domain.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.group4.DLS.domain.entity.enums.AssignmentStatus;

import com.group4.DLS.domain.entity.enums.Status;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "assignments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class Assignment {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.UUID)
    String assignmentId;

    String assignmentName;

    String descriptionAssignment;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    AssignmentStatus assignmentStatus = AssignmentStatus.OPEN;

    @Enumerated(EnumType.STRING)
    Status status;

    LocalDate createdAt;

    LocalDate updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDate.now();
        this.updatedAt = LocalDate.now();

        if (assignmentStatus == null) {
            this.assignmentStatus = AssignmentStatus.OPEN;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDate.now();
    }

    // One project has Many Dataset
    @OneToMany(mappedBy = "assignment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Dataset> datasets = new ArrayList<>();

    // One project has Many Task
    @OneToMany(mappedBy = "assignment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Task> tasks = new ArrayList<>();

    // Many Assignment belongs to One Project
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id" , nullable = false)
    @JsonIgnore
    private Project project;

    // Many Assignment belongs to One Dataset
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dataset_id", nullable = false)
    @JsonIgnore
    private Dataset dataset;
}
