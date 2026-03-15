package com.group4.DLS.domain.entity;

import java.time.LocalDateTime;

import com.group4.DLS.domain.enums.TaskDataItemStatus;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Table(
    name = "task_items",
    uniqueConstraints = @UniqueConstraint(columnNames = {"task_id", "dataitem_id"})
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class TaskDataItem {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "task_item_id")
    String taskItemId;

    @Column(name= "item_index")
    Integer itemIndex;

    // Many TaskItem belongs to One Task
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false)
    Task task;

    // Many TaskItem belongs to One Dataitem
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dataitem_id", nullable = false)
    Dataitem dataitem;

    @Enumerated(EnumType.STRING)
    @Column(name = "item_status")
    TaskDataItemStatus taskDataItemStatus;

    @Column(name = "assigned_at")
    LocalDateTime assignedAt;

    @Column(name = "completed_at")
    LocalDateTime completedAt;

    @PrePersist
    protected void onAssign() {
        this.assignedAt = LocalDateTime.now();
        if (this.taskDataItemStatus == null) {
            this.taskDataItemStatus = TaskDataItemStatus.PENDING;
        }
    }
}
