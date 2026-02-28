package com.group4.DLS.domain.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.group4.DLS.domain.entity.enums.AnnotationConfidence;
import com.group4.DLS.domain.entity.enums.AnnotationStatus;
import com.group4.DLS.domain.entity.enums.AnnotationType;
import com.group4.DLS.domain.entity.enums.ReviewStatus;

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
@Table(name = "annotations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class Annotation {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.UUID)
    String annotationId;

    @Enumerated(EnumType.STRING)
    AnnotationConfidence annotationConfidence;

    String comment;

    @Enumerated(EnumType.STRING)
    AnnotationType annotationType;

    @Column(name = "annotation_data", columnDefinition = "JSON")
    String annotationData;

    boolean flagForReview;

    @Enumerated(EnumType.STRING)
    ReviewStatus reviewStatus;

    @Enumerated(EnumType.STRING)
    AnnotationStatus annotationStatus;

    @Column(nullable = false, unique = true)
    int version;

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

    // One Annotation has Many Review
    @OneToMany(mappedBy = "annotation", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Review> reviews = new ArrayList<>();

    // Many Annotation belongs to One Task
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "taskId", nullable = false)
    private Task task;
}
