package com.group4.DLS.domain.entity;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "labels")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class Label {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.UUID)
    String labelId;

    @Column(nullable = false, unique = true)
    String labelName;

    String color;

    String description;

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

    // Many Labels belongs to One Label_Schema
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schemaId", nullable = false)
    private LabelSchema labelSchema;

    // Many Labels belongs to One Annotation_Object
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "annotationObjectId")
    @JsonIgnore
    private AnnotationObject annotationObject;
}
