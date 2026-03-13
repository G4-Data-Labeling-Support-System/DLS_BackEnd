package com.group4.DLS.domain.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.group4.DLS.domain.enums.DataItemStatus;
import com.group4.DLS.domain.enums.DataType;
import com.group4.DLS.domain.enums.FileFormat;

import com.group4.DLS.domain.enums.ProjectStatus;
import jakarta.persistence.CascadeType;
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
import jakarta.persistence.OneToMany;
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
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "item_id")
    String itemId;

    @Column(name = "file_name", nullable = false, unique = true)
    String fileName;

    @Column(name = "url", nullable = false)
    String url;

    @Enumerated(EnumType.STRING)
    @Column(name = "file_format", nullable = false)
    FileFormat fileFormat;

    @Column(name = "file_size")
    int fileSize;

    @Column(name = "width")
    int width;

    @Column(name = "height")
    int height;

    @Enumerated(EnumType.STRING)
    @Column(name = "data_type")
    DataType dataType;

    @Column(name = "uploaded_at")
    LocalDateTime uploadedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "dataitem_status", nullable = false)
    DataItemStatus dataItemStatus;

    @PrePersist
    protected void onCreate() {

        this.uploadedAt = LocalDateTime.now();

            if (dataItemStatus == null) {
                this.dataItemStatus = DataItemStatus.ACTIVE;
            }
    }

    // Many Dataitem belongs to One Dataset
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "datasetId", nullable = false)
    @JsonIgnore
    private Dataset dataset;

    // One DataItem has Many Annotation
    @OneToMany(mappedBy = "dataitem", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Annotation> annotations = new ArrayList<>();

    // One DataItem has Many TaskDataitems
    @OneToMany(mappedBy = "dataitem", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TaskDataItem> taskDataItems = new ArrayList<>();
}
