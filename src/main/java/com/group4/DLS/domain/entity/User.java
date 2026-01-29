package com.group4.DLS.domain.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.group4.DLS.domain.entity.enums.UserRole;
import com.group4.DLS.domain.entity.enums.UserStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(nullable = false, unique = true)
    String username;

    String fullName;

    @Column(nullable = false)
    String password;

    @Column(nullable = false, unique = true)
    String email;

    String coverImage;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    UserRole userRole;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    UserStatus status;

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

    // One user has Many Activity_Logs
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ActivityLog> activityLogs = new ArrayList<>();

    // One user has Many Activity_Logs
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Task> tasks = new ArrayList<>();

    // One user has Many Review
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Review> reviews = new ArrayList<>();
}
