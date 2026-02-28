package com.group4.DLS.domain.entity;

import java.time.LocalDateTime;

import com.group4.DLS.domain.entity.enums.DataType;
import com.group4.DLS.domain.entity.enums.FileFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
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
@Table(name = "dataitems")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class Dataitem {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.UUID)
    String itemId;

    @Column(nullable = false, unique = true)
    String fileName;

    @Column(nullable = false)
    String url;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    FileFormat fileFormat;

    int fileSize;

    int width;

    int height;

    @Column(nullable = true)
    DataType dataType;

    LocalDateTime uploadedAt;

    @PrePersist
    protected void onCreate() {
        this.uploadedAt = LocalDateTime.now();
    }

    // Many Dataitem belongs to One Dataset
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "datasetId", nullable = false)
    private Dataset dataset;
}
