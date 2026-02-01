
package com.group4.DLS.repository;

import com.group4.DLS.domain.entity.Guideline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GuidelineRepository extends JpaRepository<Guideline, String> {
    @Query("""
        SELECT g
        FROM Guideline g
        WHERE g.project.projectId = :projectId
        ORDER BY g.version DESC
    """)
    Optional<Guideline> findTopByProjectIdOrderByVersionDesc(@Param("projectId") String projectId);
    // Lấy tất cả guideline của 1 project
    @Query("""
        SELECT g
        FROM Guideline g
        WHERE g.project.projectId = :projectId
        ORDER BY g.version ASC
    """)
    List<Guideline> findAllByProjectId(@Param("projectId") String projectId);
}
