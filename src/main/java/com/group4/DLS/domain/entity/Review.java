package com.group4.DLS.domain.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.group4.DLS.domain.enums.ReviewStatus;

import jakarta.persistence.*;
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
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "review_id")
    String reviewId;

    @Enumerated(EnumType.STRING)
    @Column(name = "review_status")
    ReviewStatus reviewStatus = ReviewStatus.IN_PROGRESS;

    @Column(name = "comment")
    String comment;

    @Column(name = "reviewed_at")
    LocalDateTime reviewedAt;

    // Many Review belongs to One User
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewer_id", nullable = false)
    private User user;

    // Many Review belongs to One Annotation
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "annotation_id", nullable = false)
    private Annotation annotation;

    // List ảnh evidence lưu trực tiếp trong Review
    @ElementCollection(fetch = FetchType.EAGER)
    @Column(name = "evidences", nullable = true)//cho phép Hibernate/JPA lưu collection đơn giản (List<String>) mà không cần tạo entity riêng.
    private List<String> evidences = new ArrayList<>();
}
