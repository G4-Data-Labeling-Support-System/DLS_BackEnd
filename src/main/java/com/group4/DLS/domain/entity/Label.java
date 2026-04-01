package com.group4.DLS.domain.entity;

import java.time.LocalDateTime;

import com.group4.DLS.domain.enums.LabelStatus;
import com.group4.DLS.domain.enums.ProjectStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "labels")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class Label {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "label_id")
    String labelId;

    @Column(name = "label_name", nullable = false)
    String labelName;

    @Column(name = "color")
    String color;

    @Column(name = "description")
    String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "label_status", nullable = false)
    LabelStatus labelStatus = LabelStatus.ACTIVE;

    @Column(name = "created_at")
    LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // Many Labels belong to One Dataset
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dataset_id", nullable = true)
    private Dataset dataset;
    
    // Many Labels belong to One Annotation
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "annotation_id", nullable = true)
    private Annotation annotation;

}
