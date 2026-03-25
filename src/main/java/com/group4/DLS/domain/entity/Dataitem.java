package com.group4.DLS.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.group4.DLS.domain.enums.DataItemStatus;
import com.group4.DLS.domain.enums.DataType;
import com.group4.DLS.domain.enums.FileFormat;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.aspectj.bridge.IMessage;

import java.time.LocalDateTime;

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

    @Enumerated(EnumType.STRING)
    @Column(name = "dataitem_status")
    DataItemStatus dataItemStatus = DataItemStatus.ACTIVE;

    @Column(name = "uploaded_at")
    LocalDateTime uploadedAt;

    @PrePersist
    protected void onCreate() {
        this.uploadedAt = LocalDateTime.now();
    }

    // Many Dataitem belongs to One Dataset
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "datasetId", nullable = false)
    @JsonIgnore
    private Dataset dataset;
}
