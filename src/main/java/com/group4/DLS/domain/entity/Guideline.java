package com.group4.DLS.domain.entity;

import java.time.LocalDateTime;

import com.group4.DLS.domain.entity.enums.GuidelineStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "guidelines")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class Guideline {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.UUID)
    String guideId;

    @Column(nullable = false)
    String title;

    @Column(nullable = false)
    String content;

    @Column(nullable = false)
    int version;

    LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    GuidelineStatus status;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // Many Guideline belongs to One Project
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "projectId", nullable = false)
    private Project project;
}
