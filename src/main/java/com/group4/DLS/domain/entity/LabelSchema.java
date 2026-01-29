package com.group4.DLS.domain.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
