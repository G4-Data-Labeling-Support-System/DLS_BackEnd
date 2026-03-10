package com.group4.DLS.services;

import com.group4.DLS.domain.dto.request.GuidelineCreateRequest;
import com.group4.DLS.domain.dto.response.GuidelineResponse;
import com.group4.DLS.domain.entity.Guideline;
import com.group4.DLS.domain.entity.Project;
import com.group4.DLS.domain.entity.enums.GuidelineStatus;
import com.group4.DLS.exceptions.AppException;
import com.group4.DLS.exceptions.enums.ErrorCode;
import com.group4.DLS.mappers.GuidelineMapper;
import com.group4.DLS.repositories.GuidelineRepository;
import com.group4.DLS.repositories.ProjectRepository;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class GuidelineService {

    GuidelineRepository guidelineRepository;
    ProjectRepository projectRepository;
    GuidelineMapper guidelineMapper;
    ActivityLogService logService;

    public GuidelineResponse create(String projectId, GuidelineCreateRequest request) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new AppException(ErrorCode.PROJECT_NOT_FOUND));

        if (guidelineRepository.existsByTitleAndProject_ProjectId(request.getTitle(), projectId)) {
            throw new AppException(ErrorCode.GUIDELINE_EXISTS);
        }


        Guideline guideline = guidelineMapper.toEntity(request);
        guideline.setStatus(GuidelineStatus.ACTIVE);
        guideline.setProject(project);
        guideline.setVersion(1);

        guidelineRepository.save(guideline);

        // Log action
        // logService.log(
        //         "CREATE_GUIDELINE",
        //         "GUIDELINE",
        //         guideline.getGuideId(),
        //         "Guideline created: " + guideline.getTitle());

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

        // Log action
        // logService.log(
        //         "UPDATE_GUIDELINE",
        //         "GUIDELINE",
        //         guideline.getGuideId(),
        //         "Guideline updated: " + guideline.getTitle());

        return guidelineMapper.toResponse(guideline);
    }

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

    public GuidelineResponse deleteGuideline(String guidelineId){
        Guideline guideline = guidelineRepository.findById(guidelineId)
                .orElseThrow(() -> new AppException(ErrorCode.GUIDELINE_NOT_FOUND));
        guideline.setStatus(GuidelineStatus.INACTIVE);
        guidelineRepository.save(guideline);

        // Log action
        // logService.log(
        //         "REMOVE_GUIDELINE",
        //         "GUIDELINE",
        //         guideline.getGuideId(),
        //         "Guideline removed: " + guideline.getTitle());

        return guidelineMapper.toResponse(guideline);
    }
}
