
package com.group4.DLS.repository;

import com.group4.DLS.domain.entity.Guideline;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GuidelineRepository extends JpaRepository<Guideline, String> {
    Optional<Guideline> findTopByProjectIdOrderByVersionDesc(String projectId);


    List<Guideline> findByProjectId(String projectId);
}
