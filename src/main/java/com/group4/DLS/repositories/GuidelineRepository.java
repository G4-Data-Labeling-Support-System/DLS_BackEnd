
package com.group4.DLS.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.group4.DLS.domain.entity.Guideline;

import java.util.List;

@Repository
public interface GuidelineRepository extends JpaRepository<Guideline, String> {
    List<Guideline> findAllByProject_ProjectId(String projectId);
    boolean existsByGuideNameAndProject_ProjectId(String guideName, String projectId);
    boolean existsByGuideNameAndProject_ProjectIdAndGuideIdNot(String guideName, String projectId, String guideId);
}
