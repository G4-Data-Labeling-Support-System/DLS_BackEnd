package com.group4.DLS.services;

import com.group4.DLS.aop.LogActivity;
import com.group4.DLS.domain.dto.request.ProjectCreationRequest;
import com.group4.DLS.domain.dto.request.ProjectStatusUpdateRequest;
import com.group4.DLS.domain.dto.request.ProjectUpdateRequest;
import com.group4.DLS.domain.dto.response.ProjectResponse;
import com.group4.DLS.domain.entity.*;
import com.group4.DLS.domain.enums.ActionType;
import com.group4.DLS.domain.enums.AssignmentStatus;
import com.group4.DLS.domain.enums.ProjectStatus;
import com.group4.DLS.exceptions.AppException;
import com.group4.DLS.exceptions.enums.ErrorCode;
import com.group4.DLS.helper.RequestUtils;
import com.group4.DLS.mappers.ProjectMapper;
import com.group4.DLS.repositories.AssignmentRepository;
import com.group4.DLS.repositories.DatasetRepository;
import com.group4.DLS.repositories.ProjectMemberRepository;
import com.group4.DLS.repositories.ProjectRepository;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProjectService {

    ProjectRepository projectRepository;
    ProjectMapper projectMapper;
    ProjectMemberRepository projectMemberRepository;

    ActivityLogService logService;
    DatasetRepository datasetRepository;
    AssignmentRepository assignmentRepository;
    DatasetService datasetService;

    // Lấy user hiện tại từ SecurityContextHolder
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return (User) authentication.getPrincipal();
    }

    //get project by dataset
    public ProjectResponse getProjectByDatasetId(String datasetId){
        Dataset dataset = datasetRepository.findById(datasetId)
                .orElseThrow(() -> new AppException(ErrorCode.DATASET_NOT_FOUND));

        Project project = dataset.getProject();

        // update status trước khi trả
        updateProjectStatus(project.getProjectId());

        return projectMapper.toProjectResponse(project);
    }

    // ================= GET ALL PROJECT THAT CURRETLY ACTIVE =================
    public List<ProjectResponse> getAllProjects() {
        List<Project> projects = projectRepository.findByProjectStatusIn(List.of(
                ProjectStatus.INACTIVE,
                ProjectStatus.NOT_STARTED,
                ProjectStatus.COMPLETED,
                ProjectStatus.IN_PROGRESS));

        // update tất cả project
        projects.forEach(p -> updateProjectStatus(p.getProjectId()));

        return projectMapper.toProjectResponse(projects);
    }



    // ================= GET PROJECT BY ID =================
    public ProjectResponse getProjectById(String projectId) {
        Project project = projectRepository.findByProjectIdAndProjectStatusIn(projectId,
                        List.of(
                                ProjectStatus.INACTIVE,
                                ProjectStatus.COMPLETED,
                                ProjectStatus.IN_PROGRESS,
                                ProjectStatus.NOT_STARTED))
                .orElseThrow(() -> new AppException(ErrorCode.PROJECT_NOT_FOUND));

        // update
        updateProjectStatus(projectId);

        return projectMapper.toProjectResponse(project);
    }

    // ================== GET PROJECTS FOR CREATION OR UPDATION DATASET ============
    public List<ProjectResponse> getAllProjectsForCrateOrUpdateDataset() {
        List<Project> projects = projectRepository.findByProjectStatusIn(List.of(
                ProjectStatus.NOT_STARTED,
                ProjectStatus.IN_PROGRESS));

        // update
        projects.forEach(p -> updateProjectStatus(p.getProjectId()));

        return projectMapper.toProjectResponse(projects);
    }
    // ================= CREATE PROJECT =================
    @LogActivity(
        action = "CREATE",
        entity = "Project",
        description = "Create project",
        entityIdField = "projectId"
    )
    public ProjectResponse createProject(
        ProjectCreationRequest request
    ) {
        User manager = getCurrentUser();

        boolean existsActiveProject = projectRepository.existsByProjectNameAndProjectStatusNot(
                request.getProjectName(),
                ProjectStatus.INACTIVE);

        if (existsActiveProject) {
            throw new AppException(ErrorCode.PROJECT_ALREADY_EXISTS);
        }

        Project project = projectMapper.createProjectFromRequest(request);

        project = projectRepository.save(project);

        // Manager auto là member của project
        ProjectMember member = new ProjectMember();
        member.setProject(project);
        member.setUser(manager);
        member.setJoinAt(LocalDateTime.now());
        projectMemberRepository.save(member);

        return projectMapper.toProjectResponse(project);
    }

    @LogActivity(
        action = "UPDATE",
        entity = "Project",
        description = "Update project",
        entityIdParam = "projectId"
    )
    // ================= UPDATE PROJECT =================
    public ProjectResponse updateProject(String projectId, ProjectUpdateRequest request) {
        Project project = projectRepository.findByProjectIdAndProjectStatusIn(
                projectId,
                List.of(
                        ProjectStatus.INACTIVE,
                        ProjectStatus.NOT_STARTED,
                        ProjectStatus.COMPLETED,
                        ProjectStatus.IN_PROGRESS,
                        ProjectStatus.NOT_STARTED))
                .orElseThrow(() -> new AppException(ErrorCode.PROJECT_NOT_FOUND));

        projectMapper.updateProjectFromRequest(request, project);
        project = projectRepository.save(project);

        return projectMapper.toProjectResponse(project);
    }

    // ================= UPDATE PROJECT STATUS =================
    @LogActivity(
        action = "UPDATE",
        entity = "Project",
        description = "Update project status",
        entityIdParam = "projectId"
    )
    public ProjectResponse updateProjectStatus(String projectId, ProjectStatusUpdateRequest request) {
        Project project = projectRepository.findByProjectIdAndProjectStatusIn(
                projectId,
                List.of(
                        ProjectStatus.INACTIVE,
                        ProjectStatus.NOT_STARTED,
                        ProjectStatus.COMPLETED,
                        ProjectStatus.IN_PROGRESS,
                        ProjectStatus.NOT_STARTED))
                .orElseThrow(() -> new AppException(ErrorCode.PROJECT_NOT_FOUND));

        if (project != null) {
            projectMapper.updateProjectStatusFromRequest(request, project);
            project = projectRepository.save(project);
        }

        return projectMapper.toProjectResponse(project);
    }

    // ================= DELETE PROJECT =================
    @LogActivity(
        action = "DELETE",
        entity = "Project",
        description = "Delete project",
        entityIdParam = "projectId"
    )
    public void deleteProject(String projectId) {
        Project project = projectRepository.findByProjectIdAndProjectStatusIn(
                        projectId,
                        List.of(
                                ProjectStatus.INACTIVE,
                                ProjectStatus.COMPLETED,
                                ProjectStatus.IN_PROGRESS,
                                ProjectStatus.NOT_STARTED))
                .orElseThrow(() -> new AppException(ErrorCode.PROJECT_NOT_FOUND));

        //  Lấy assignments của project
        List<Assignment> assignments = assignmentRepository.findByProject_ProjectId(projectId);

        //  Check điều kiện
        boolean hasActiveAssignment = assignments.stream()
                .anyMatch(a -> a.getAssignmentStatus() == AssignmentStatus.IN_PROGRESS
                        || a.getAssignmentStatus() == AssignmentStatus.REVIEWING);

        if (hasActiveAssignment) {
            throw new AppException(ErrorCode.PROJECT_CANNOT_DELETE_WHEN_ASSIGNMENT_ACTIVE);
        }

        //  Nếu OK thì xóa dataset
        project.getDatasets()
                .forEach(d -> datasetService.deleteDataset(d.getDatasetId()));

        //  Set inactive
        project.setProjectStatus(ProjectStatus.INACTIVE);

        projectRepository.save(project);
    }

    public void updateProjectStatus(String projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new AppException(ErrorCode.PROJECT_NOT_FOUND));

        List<Assignment> assignments = assignmentRepository.findByProject_ProjectId(projectId);

        boolean allNotStarted = assignments.stream()
                .allMatch(a -> a.getAssignmentStatus() == AssignmentStatus.ASSIGNED);

        boolean anyInProgress = assignments.stream()
                .anyMatch(a -> a.getAssignmentStatus() == AssignmentStatus.IN_PROGRESS);

        boolean anyReviewing = assignments.stream()
                .anyMatch(a -> a.getAssignmentStatus() == AssignmentStatus.REVIEWING);

        boolean allDone = assignments.stream()
                .allMatch(a -> a.getAssignmentStatus() == AssignmentStatus.COMPLETED);

        // Nếu project bị inactive thì giữ nguyên
        if (project.getProjectStatus() == ProjectStatus.INACTIVE) {
            project.setProjectStatus(ProjectStatus.INACTIVE);

        } else if (allDone && !assignments.isEmpty()) {
            project.setProjectStatus(ProjectStatus.COMPLETED);

        } else if (anyReviewing) {
            project.setProjectStatus(ProjectStatus.IN_PROGRESS); // hoặc REVIEWING nếu bạn có

        } else if (anyInProgress) {
            project.setProjectStatus(ProjectStatus.IN_PROGRESS);

        } else if (allNotStarted) {
            project.setProjectStatus(ProjectStatus.NOT_STARTED);
        }

        projectRepository.save(project);
    }
}
