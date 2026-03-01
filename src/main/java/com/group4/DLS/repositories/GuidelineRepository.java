
package com.group4.DLS.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.group4.DLS.domain.entity.Guideline;

import java.util.List;

@Repository
public interface GuidelineRepository extends JpaRepository<Guideline, String> {
    List<Guideline> findAllByProject_ProjectId(String projectId);
    boolean existsByTitleAndProject_ProjectId(String title, String projectId);
    boolean existsByTitleAndProject_ProjectIdAndGuideIdNot(String title, String projectId, String guideId);
}
