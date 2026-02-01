package com.group4.DLS.services;

import com.group4.DLS.domain.dto.request.GuidelineCreateRequest;
import com.group4.DLS.domain.dto.response.GuidelineResponse;
import com.group4.DLS.domain.entity.Guideline;
import com.group4.DLS.domain.entity.Project;
import com.group4.DLS.mappers.GuidelineMapper;
import com.group4.DLS.repositories.GuidelineRepository;
import com.group4.DLS.repositories.ProjectRepository;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class GuidelineService {

    GuidelineRepository guidelineRepository;
    ProjectRepository projectRepository;
    GuidelineMapper guidelineMapper;

    public GuidelineResponse create(String projectId, GuidelineCreateRequest request) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        int nextVersion = guidelineRepository
                .findTopByProjectIdOrderByVersionDesc(projectId)
                .map(g -> g.getVersion() + 1)
                .orElse(1);

        Guideline guideline = guidelineMapper.toEntity(request);
        guideline.setProject(project);
        guideline.setVersion(nextVersion);

        guidelineRepository.save(guideline);

        return guidelineMapper.toResponse(guideline);
    }

    public GuidelineResponse getLatest(String projectId) {
        Guideline guideline = guidelineRepository
                .findTopByProjectIdOrderByVersionDesc(projectId)
                .orElseThrow(() -> new RuntimeException("Guideline not found"));

        return guidelineMapper.toResponse(guideline);
    }

    public List<GuidelineResponse> getAllByProject(String projectId) {
        return guidelineRepository.findAllByProjectId(projectId).stream()
                .map(guidelineMapper::toResponse)
                .toList();
    }

    public List<Guideline> getAllGuideline(){
        return guidelineRepository.findAll();
    }
}
