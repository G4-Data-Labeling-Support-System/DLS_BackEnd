package com.group4.DLS.domain.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.group4.DLS.domain.entity.enums.GuidelineStatus;
import com.group4.DLS.domain.entity.enums.LabelSchemaStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "label_schemas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class LabelSchema {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.UUID)
    String schemaId;

    @Column(nullable = false)
    String schemaName;

    String description;

    @Column(nullable = false, unique = true)
    int version;

    LocalDate createdAt;

    LocalDate updatedAt;

    @Enumerated(EnumType.STRING)
    LabelSchemaStatus status = LabelSchemaStatus.ACTIVE;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDate.now();
        this.updatedAt = LocalDate.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDate.now();
    }

    // One LabelSchema has Many Label
    @OneToMany(mappedBy = "labelSchema", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Label> labels = new ArrayList<>();

    // Many LabelSchema belongs to One Project
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "projectId")
    private Project project;
}
