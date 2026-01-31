package com.group4.DLS.service;

import com.group4.DLS.domain.dto.request.GuidelineCreateRequest;
import com.group4.DLS.domain.dto.response.GuidelineResponse;
import com.group4.DLS.domain.entity.Guideline;
import com.group4.DLS.domain.entity.Project;
import com.group4.DLS.repository.GuidelineRepository;
import com.group4.DLS.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)

public class GuidelineService {

    private final GuidelineRepository guidelineRepository;
    private final ProjectRepository projectRepository;

    public GuidelineResponse create(String projectId, GuidelineCreateRequest request) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        // Business rule: mỗi project chỉ có 1 guideline active
        Optional<Guideline> latest = guidelineRepository
                .findTopByProjectIdOrderByVersionDesc(projectId);

        int nextVersion = latest.map(g -> g.getVersion() + 1).orElse(1);

        Guideline guideline = new Guideline();
        guideline.setProject(project);
        guideline.setContent(request.getContent());
        guideline.setVersion(nextVersion);

        guidelineRepository.save(guideline);

        return mapToResponse(guideline);
    }

    public GuidelineResponse getLatest(String projectId) {
        Guideline guideline = guidelineRepository
                .findTopByProjectIdOrderByVersionDesc(projectId)
                .orElseThrow(() -> new RuntimeException("Guideline not found"));

        return mapToResponse(guideline);
    }

    public List<GuidelineResponse> getAllByProject(String projectId) {
        return guidelineRepository.findByProjectId(projectId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private GuidelineResponse mapToResponse(Guideline g) {
        return new GuidelineResponse(
                g.getGuideId(),
                g.getProject().getProjectId(),
                g.getContent(),
                g.getVersion(),
                g.getCreatedAt(),
                g.getUpdatedAt()
        );
    }

}

