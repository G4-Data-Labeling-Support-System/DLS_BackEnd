
package com.group4.DLS.repository;

import com.group4.DLS.domain.entity.Guideline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GuidelineRepository extends JpaRepository<Guideline, String> {
    List<Guideline> findAllByProject_ProjectId(String projectId);
    boolean existsByGuideName(String guidelineName);
}
