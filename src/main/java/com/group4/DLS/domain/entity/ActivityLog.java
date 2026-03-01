package com.group4.DLS.domain.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
    @Column(name = "log_id")
    String logId;

    @Column(name = "action", nullable = false)
    String action;

    @Column(name = "entity_name", nullable = false)
    String entityName;

    @Column(name = "entity_id", nullable = false)
    String entityId;

    @Column(name = "description", nullable = false)
    String description;

    @Column(name = "ip_address", nullable = false)
    String ipAddress;

    @Column(name = "timestamp", nullable = false)
    LocalDateTime timestamp;

    @PrePersist
    protected void onAction() {
        this.timestamp = LocalDateTime.now();
    }

    // Many Activity Logs belongs to One user
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}