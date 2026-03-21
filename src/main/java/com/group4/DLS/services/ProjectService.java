package com.group4.DLS.services;

import com.group4.DLS.aop.LogActivity;
import com.group4.DLS.domain.dto.request.ProjectCreationRequest;
import com.group4.DLS.domain.dto.request.ProjectStatusUpdateRequest;
import com.group4.DLS.domain.dto.request.ProjectUpdateRequest;
import com.group4.DLS.domain.dto.response.ProjectResponse;
import com.group4.DLS.domain.entity.Project;
import com.group4.DLS.domain.entity.ProjectMember;
import com.group4.DLS.domain.entity.User;
import com.group4.DLS.domain.enums.ActionType;
import com.group4.DLS.domain.enums.ProjectStatus;
import com.group4.DLS.exceptions.AppException;
import com.group4.DLS.exceptions.enums.ErrorCode;
import com.group4.DLS.helper.RequestUtils;
import com.group4.DLS.mappers.ProjectMapper;
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

    // Lấy user hiện tại từ SecurityContextHolder
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return (User) authentication.getPrincipal();
    }

    // ================= GET ALL PROJECT THAT CURRETLY ACTIVE =================
    public List<ProjectResponse> getAllProjects() {
        List<Project> projects = projectRepository.findByProjectStatusIn(List.of(
                ProjectStatus.INACTIVE,
                ProjectStatus.NOT_STARTED,
                ProjectStatus.COMPLETED,
                ProjectStatus.IN_PROGRESS,
                ProjectStatus.NOT_STARTED));
        return projectMapper.toProjectResponse(projects);
    }

    // ================= GET PROJECT BY ID =================
    public ProjectResponse getProjectById(String projectId) {
        Project project = projectRepository.findByProjectIdAndProjectStatusIn(projectId,
                List.of(
                        ProjectStatus.INACTIVE,
                        ProjectStatus.NOT_STARTED,
                        ProjectStatus.COMPLETED,
                        ProjectStatus.IN_PROGRESS,
                        ProjectStatus.NOT_STARTED))
                .orElseThrow(() -> new AppException(ErrorCode.PROJECT_NOT_FOUND));

        return projectMapper.toProjectResponse(project);
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
                        ProjectStatus.NOT_STARTED,
                        ProjectStatus.COMPLETED,
                        ProjectStatus.IN_PROGRESS,
                        ProjectStatus.NOT_STARTED))
                .orElseThrow(() -> new AppException(ErrorCode.PROJECT_NOT_FOUND));

        project.setProjectStatus(ProjectStatus.INACTIVE);
        
        projectRepository.save(project);
    }
}
