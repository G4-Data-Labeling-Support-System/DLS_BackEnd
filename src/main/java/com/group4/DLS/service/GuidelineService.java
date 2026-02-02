package com.group4.DLS.service;

import com.group4.DLS.domain.dto.request.GuidelineCreateRequest;
import com.group4.DLS.domain.dto.response.GuidelineResponse;
import com.group4.DLS.domain.entity.Guideline;
import com.group4.DLS.domain.entity.Project;
import com.group4.DLS.exceptions.AppException;
import com.group4.DLS.exceptions.enums.ErrorCode;
import com.group4.DLS.mapper.GuidelineMapper;
import com.group4.DLS.repository.GuidelineRepository;
import com.group4.DLS.repository.ProjectRepository;
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
                .orElseThrow(() -> new AppException(ErrorCode.PROJECT_NOT_FOUND));

        Guideline guideline = guidelineMapper.toEntity(request);
        guideline.setProject(project);
        guideline.setVersion(1);

        guidelineRepository.save(guideline);

        return guidelineMapper.toResponse(guideline);
    }

    public List<GuidelineResponse> getAllByProject(String projectId) {
        if(projectRepository.findById(projectId).isEmpty()){
            throw new AppException(ErrorCode.PROJECT_NOT_FOUND);
        }
        List<Guideline> guidelines = guidelineRepository.findAllByProject_ProjectId(projectId);
        return guidelines.stream()
                .map(guidelineMapper::toResponse)
                .toList();
    }

    public GuidelineResponse update(String guidelineId, GuidelineCreateRequest request) {
        Guideline guideline = guidelineRepository.findById(guidelineId).orElseThrow(() ->
                new AppException(ErrorCode.GUIDELINE_NOT_FOUND));

        if (guidelineRepository.existsByGuideName(request.getGuideName())){
            throw new AppException(ErrorCode.GUIDELINE_EXISTS);
        }

        guideline = guidelineMapper.toEntity(request);
        guideline.setVersion(guideline.getVersion() + 1);
        guidelineRepository.save(guideline);

        return guidelineMapper.toResponse(guideline);
    }

    public List<Guideline> getAllGuideline(){
        List<Guideline> guidelines = guidelineRepository.findAll();
        return guidelines;
    }
}
