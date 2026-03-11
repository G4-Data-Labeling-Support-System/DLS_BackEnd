package com.group4.DLS.domain.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.group4.DLS.domain.enums.AnnotationConfidence;
import com.group4.DLS.domain.enums.AnnotationStatus;
import com.group4.DLS.domain.enums.AnnotationType;
import com.group4.DLS.domain.enums.ReviewStatus;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "annotations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class Annotation {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "annotation_id")
    String annotationId;

    @Enumerated(EnumType.STRING)
    @Column(name = "annotation_confidence")
    AnnotationConfidence annotationConfidence;

    @Column(name = "comment")
    String comment;

    @Enumerated(EnumType.STRING)
    @Column(name = "annotation_type")
    AnnotationType annotationType;

    @Column(name = "annotation_data", columnDefinition = "JSON")
    String annotationData;

    @Enumerated(EnumType.STRING)
    @Column(name = "annotation_status")
    AnnotationStatus annotationStatus;

    @Column(name = "created_at")
    LocalDate createdAt;

    @Column(name = "updated_at")
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

    // Many Annotation belongs to One Task
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    // Many Annotation belongs to One DataItem
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private Dataitem dataitem;

    // Many Annotation belongs to One User
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // One Annotation has Many Labels 
    @ManyToMany
    @JoinTable(
            name = "annotation_labels",
            joinColumns = @JoinColumn(name = "annotation_id"),
            inverseJoinColumns = @JoinColumn(name = "label_id")
    )
    private List<Label> labels = new ArrayList<>();

    // One Annotation has Many Review
    @OneToMany(mappedBy = "annotation", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Review> reviews = new ArrayList<>();

}
