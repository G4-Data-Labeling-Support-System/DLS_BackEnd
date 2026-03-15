package com.group4.DLS.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.group4.DLS.domain.enums.DataItemStatus;
import com.group4.DLS.domain.enums.DataType;
import com.group4.DLS.domain.enums.FileFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "task_data_item_id")
    private TaskDataItem taskDataItem;
}
