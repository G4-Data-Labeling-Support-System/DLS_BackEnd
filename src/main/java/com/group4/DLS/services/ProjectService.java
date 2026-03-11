package com.group4.DLS.services;

import com.group4.DLS.domain.dto.request.ProjectCreationRequest;
import com.group4.DLS.domain.dto.request.ProjectStatusUpdateRequest;
import com.group4.DLS.domain.dto.request.ProjectUpdateRequest;
import com.group4.DLS.domain.dto.response.ProjectResponse;
import com.group4.DLS.domain.entity.Project;
import com.group4.DLS.domain.entity.ProjectMember;
import com.group4.DLS.domain.entity.User;
import com.group4.DLS.domain.enums.ProjectStatus;
import com.group4.DLS.exceptions.AppException;
import com.group4.DLS.exceptions.enums.ErrorCode;
import com.group4.DLS.mappers.ProjectMapper;
import com.group4.DLS.repositories.ProjectMemberRepository;
import com.group4.DLS.repositories.ProjectRepository;
import com.group4.DLS.security.CurrentUserProvider;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProjectService {

    ProjectRepository projectRepository;
    ProjectMapper projectMapper;
    ProjectMemberRepository projectMemberRepository;
    CurrentUserProvider currentUserProvider;

    ActivityLogService logService;

    // ================= GET ALL PROJECT THAT CURRETLY ACTIVE =================
    public List<ProjectResponse> getAllProjects() {
        List<Project> projects = projectRepository.findByStatusIn(List.of(
                ProjectStatus.INACTIVE,
                ProjectStatus.ACTIVE,
                ProjectStatus.CANCELLED,
                ProjectStatus.COMPLETED,
                ProjectStatus.IN_PROGRESS,
                ProjectStatus.NOT_STARTED,
                ProjectStatus.ON_HOLD));
        return projectMapper.toProjectResponse(projects);
    }

    // ================= GET PROJECT BY ID =================
    public ProjectResponse getProjectById(String projectId) {
        Project project = projectRepository.findByProjectIdAndStatusIn(projectId,
                List.of(
                        ProjectStatus.INACTIVE,
                        ProjectStatus.ACTIVE,
                        ProjectStatus.CANCELLED,
                        ProjectStatus.COMPLETED,
                        ProjectStatus.IN_PROGRESS,
                        ProjectStatus.NOT_STARTED,
                        ProjectStatus.ON_HOLD))
                .orElseThrow(() -> new AppException(ErrorCode.PROJECT_NOT_FOUND));

        return projectMapper.toProjectResponse(project);
    }

    // ================= CREATE PROJECT =================
    public ProjectResponse createProject(ProjectCreationRequest request) {
        User manager = currentUserProvider.getCurrentUser();

        boolean existsActiveProject = projectRepository.existsByProjectNameAndStatusNot(
            request.getProjectName(), 
            ProjectStatus.INACTIVE
        );

        if (existsActiveProject) {
            throw new AppException(ErrorCode.PROJECT_ALREADY_EXISTS);
        }

        Project project = projectMapper.createProjectFromRequest(request);

        project = projectRepository.save(project);

        // Manager auto là member của project
        ProjectMember member = new ProjectMember();
        member.setProject(project);
        member.setUser(manager);
        projectMemberRepository.save(member);

        // Log action
        // logService.log(
        // "CREATE_PROJECT",
        // "PROJECT",
        // project.getProjectId(),
        // "Created project: " + project.getProjectName());

        return projectMapper.toProjectResponse(project);
    }

    // ================= UPDATE PROJECT =================
    public ProjectResponse updateProject(String projectId, ProjectUpdateRequest request) {
        Project project = projectRepository.findByProjectIdAndStatusIn(
                projectId,
                List.of(
                        ProjectStatus.ACTIVE,
                        ProjectStatus.CANCELLED,
                        ProjectStatus.COMPLETED,
                        ProjectStatus.IN_PROGRESS,
                        ProjectStatus.NOT_STARTED,
                        ProjectStatus.ON_HOLD))
                .orElseThrow(() -> new AppException(ErrorCode.PROJECT_NOT_FOUND));

        projectMapper.updateProjectFromRequest(request, project);
        project = projectRepository.save(project);

        // Log action
        // logService.log(
        // "UPDATE_PROJECT",
        // "PROJECT",
        // project.getProjectId(),
        // "Updated project: " + project.getProjectName());

        return projectMapper.toProjectResponse(project);
    }

    // ================= UPDATE PROJECT STATUS =================
    public ProjectResponse updateProjectStatus(String projectId, ProjectStatusUpdateRequest request) {
        Project project = projectRepository.findByProjectIdAndStatusIn(
                projectId,
                List.of(
                        ProjectStatus.INACTIVE,
                        ProjectStatus.ACTIVE,
                        ProjectStatus.CANCELLED,
                        ProjectStatus.COMPLETED,
                        ProjectStatus.IN_PROGRESS,
                        ProjectStatus.NOT_STARTED,
                        ProjectStatus.ON_HOLD))
                .orElseThrow(() -> new AppException(ErrorCode.PROJECT_NOT_FOUND));

        if (project != null) {
            projectMapper.updateProjectStatusFromRequest(request, project);
            project = projectRepository.save(project);

            // Log action
            // logService.log(
            // "UPDATE_PROJECT_STATUS",
            // "PROJECT",
            // project.getProjectId(),
            // "Updated project: " + project.getProjectName() + " -> " + project.getStatus()
            // );
        }

        return projectMapper.toProjectResponse(project);
    }

    // ================= DELETE PROJECT =================
    public void deleteProject(String projectId) {
        Project project = projectRepository.findByProjectIdAndStatusIn(
                projectId,
                List.of(
                        ProjectStatus.ACTIVE,
                        ProjectStatus.CANCELLED,
                        ProjectStatus.COMPLETED,
                        ProjectStatus.IN_PROGRESS,
                        ProjectStatus.NOT_STARTED,
                        ProjectStatus.ON_HOLD))
                .orElseThrow(() -> new AppException(ErrorCode.PROJECT_NOT_FOUND));

        project.setStatus(ProjectStatus.INACTIVE);
        projectRepository.save(project);

        // Log action
        // logService.log(
        // "REMOVE_PROJECT",
        // "PROJECT",
        // project.getProjectId(),
        // "Project removed: " + project.getProjectName());
    }
}
