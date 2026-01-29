package com.group4.DLS.domain.entity;

import java.time.LocalDate;

import com.group4.DLS.domain.entity.enums.ExportFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "exports")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class Export {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.UUID)
    String exportId;

    String datasetId;

    String userId;

    @Enumerated(EnumType.STRING)
    ExportFormat exportFormat;

    String fileUri;

    LocalDate createdDate;
}
