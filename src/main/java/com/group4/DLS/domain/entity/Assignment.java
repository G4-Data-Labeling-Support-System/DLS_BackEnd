package com.group4.DLS.domain.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.group4.DLS.domain.enums.AssignmentStatus;
import com.group4.DLS.domain.enums.Status;

import jakarta.persistence.*;
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
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "assignment_id")
    String assignmentId;

    @Column(name = "assignment_name")
    String assignmentName;

    @Column(name = "total_items")
    int totalItems;

    @Column(name = "completed_items")
    int completedItems;

    @Column(name = "description")
    String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "assignment_status", nullable = false)
    AssignmentStatus assignmentStatus;

    @Column(name = "due_date")
    LocalDateTime dueDate;

    @Column(name = "created_at")
    LocalDateTime createdAt;

    @Column(name = "updated_at")
    LocalDateTime updateAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();

        if (assignmentStatus == null) {
            this.assignmentStatus = AssignmentStatus.ASSIGNED;
        }
    }

    // Many Assignment belongs to One Project
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    @JsonIgnore
    private Project project;

    // One Dataset has One Assignment
    @OneToOne
    @JoinColumn(name = "dataset_id", nullable = true)
    private Dataset dataset;

    // Who created the assignment
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_by", nullable = false)
    @JsonBackReference
    private User assignedBy;

    // Who is assigned to do it
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_to", nullable = false)
    @JsonBackReference
    private User assignedTo;

    // Who is review to do it
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewed_by", nullable = false)
    @JsonBackReference
    private User reviewedBy;

    // One project has Many Task
    @OneToMany(mappedBy = "assignment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Task> tasks = new ArrayList<>();

}
