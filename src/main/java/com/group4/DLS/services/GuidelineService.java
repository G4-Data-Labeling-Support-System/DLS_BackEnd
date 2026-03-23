package com.group4.DLS.services;

import com.group4.DLS.aop.LogActivity;
import com.group4.DLS.domain.dto.request.GuidelineCreateRequest;
import com.group4.DLS.domain.dto.response.GuidelineResponse;
import com.group4.DLS.domain.entity.Guideline;
import com.group4.DLS.domain.entity.Project;
import com.group4.DLS.domain.enums.GuidelineStatus;
import com.group4.DLS.exceptions.AppException;
import com.group4.DLS.exceptions.enums.ErrorCode;
import com.group4.DLS.mappers.GuidelineMapper;
import com.group4.DLS.repositories.GuidelineRepository;
import com.group4.DLS.repositories.ProjectRepository;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class GuidelineService {

    GuidelineRepository guidelineRepository;
    ProjectRepository projectRepository;
    GuidelineMapper guidelineMapper;

    // ===== CREATE GUIDELINE =====
    @LogActivity(
        action = "CREATE",
        entity = "Guideline",
        description = "Create dataset",
        entityIdField = "guidelineId"
    )
    public GuidelineResponse create(String projectId, GuidelineCreateRequest request) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new AppException(ErrorCode.PROJECT_NOT_FOUND));

        if (guidelineRepository.existsByTitleAndProject_ProjectId(request.getTitle(), projectId)) {
            throw new AppException(ErrorCode.GUIDELINE_EXISTS);
        }

        Guideline guideline = guidelineMapper.toEntity(request);
        guideline.setProject(project);
        guideline.setVersion(1);

        guidelineRepository.save(guideline);

        return guidelineMapper.toResponse(guideline);
    }

    // ===== GET ALL GUIDELINE FOR CURRENT PROJECT =====
    public List<GuidelineResponse> getAllByProject(String projectId) {
        if(projectRepository.findById(projectId).isEmpty()){
            throw new AppException(ErrorCode.PROJECT_NOT_FOUND);
        }
        List<Guideline> guidelines = guidelineRepository.findAllByProject_ProjectId(projectId);
        return guidelines.stream()
                .map(guidelineMapper::toResponse)
                .toList();
    }

    // ===== UPDATE GUIDELINE =====
    @LogActivity(
        action = "UPDATE",
        entity = "Guideline",
        description = "Update dataset",
        entityIdParam = "guidelineId"
    )
    public GuidelineResponse update(String guidelineId, GuidelineCreateRequest request) {
        Guideline guideline = guidelineRepository.findById(guidelineId)
                .orElseThrow(() -> new AppException(ErrorCode.GUIDELINE_NOT_FOUND));

        if (guidelineRepository.existsByTitleAndProject_ProjectIdAndGuideIdNot(request.getTitle(), guideline.getProject().getProjectId(), guidelineId)) {
            throw new AppException(ErrorCode.GUIDELINE_EXISTS);
        }

        // update field, KHÔNG tạo entity mới
        guideline.setTitle(request.getTitle());
        guideline.setContent(request.getContent());
        guideline.setVersion(guideline.getVersion() + 1);

        guidelineRepository.save(guideline);

        return guidelineMapper.toResponse(guideline);
    }

    // ===== GET ALL GUIDELINE =====
    public List<GuidelineResponse> getAllGuideline(){
        List<GuidelineResponse> guidelines = guidelineRepository.findAll()
                .stream()
                .map(guidelineMapper::toResponse)
                .toList();
        if(guidelines.isEmpty()){
            throw new AppException(ErrorCode.GUIDELINE_NOT_FOUND);
        }
        return guidelines;
    }

    // ===== REMOVE GUIDELINE =====
    @LogActivity(
        action = "DELETE",
        entity = "Guideline",
        description = "Delete guideline",
        entityIdParam = "guidelineId"
    )
    public GuidelineResponse deleteGuideline(String guidelineId){
        Guideline guideline = guidelineRepository.findById(guidelineId)
                .orElseThrow(() -> new AppException(ErrorCode.GUIDELINE_NOT_FOUND));
        guideline.setGuidelineStatus(GuidelineStatus.INACTIVE);
        guidelineRepository.save(guideline);

        return guidelineMapper.toResponse(guideline);
    }
}
