package com.group4.DLS.domain.entity;

import java.time.LocalDate;

import com.group4.DLS.domain.entity.enums.ReviewDecision;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "reviews")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class Review {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.UUID)
    String reviewId;

    @Enumerated(EnumType.STRING)
    ReviewDecision reviewDecision;

    String comment;

    LocalDate reviewedAt;

    LocalDate updatedAt;

    @PrePersist
    protected void onCreate() {
        this.reviewedAt = LocalDate.now();
        this.updatedAt = LocalDate.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDate.now();
    }

    // Many Review belongs to One User
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    // Many Review belongs to One Annotation
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "annotationId", nullable = false)
    private Annotation annotation;
}
