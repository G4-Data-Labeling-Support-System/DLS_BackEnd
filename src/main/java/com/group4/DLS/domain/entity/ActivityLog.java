package com.group4.DLS.domain.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Builder
@Table(name = "activity_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class ActivityLog {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String logId;

    @Column(nullable = false)
    String username;

    @Column(nullable = false)
    String action;

    @Column(nullable = false)
    String entityName;

    @Column(nullable = false)
    String entityId;

    @Column(nullable = false)
    String description;

    @Column(nullable = false)
    String ipAddress;

    @Column(nullable = false)
    LocalDateTime timestamp;

    @PrePersist
    protected void onAction() {
        this.timestamp = LocalDateTime.now();
    }
}