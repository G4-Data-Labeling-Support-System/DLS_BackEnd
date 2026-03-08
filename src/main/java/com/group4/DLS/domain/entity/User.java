package com.group4.DLS.domain.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
    @Column(name = "user_id")
    String userId;

    @Column(name = "username", nullable = false)
    String username;

    @Column(name = "email", nullable = false, unique = true)
    String email;

    @Column(name = "password", nullable = false)
    String password;

    @Column(name = "cover_image")
    String coverImage;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    UserRole role = UserRole.ANNOTATOR;

    @Column(name = "specialization")
    String specialization;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_status", nullable = false)
    UserStatus userStatus = UserStatus.ACTIVE;

    @Column(name = "created_at", nullable = false)
    LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        
        if (this.userStatus == null) {
            this.userStatus = UserStatus.ACTIVE;
        }

        if (this.role == null) {
            this.role = UserRole.ANNOTATOR;
        }
    }

    // One user has Many Activity_Logs
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ActivityLog> activityLogs = new ArrayList<>();

    // One user has Many Review
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Review> reviews = new ArrayList<>();

    // One user has Many Annotation
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Annotation> annotations = new ArrayList<>();

    // Assignments this user created
    @OneToMany(mappedBy = "assignedBy", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Assignment> createdAssignments = new ArrayList<>();

    // Assignments assigned to this user
    @OneToMany(mappedBy = "assignedTo", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Assignment> receivedAssignments = new ArrayList<>();
}
