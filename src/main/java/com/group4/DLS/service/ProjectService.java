package com.group4.DLS.service;

import com.group4.DLS.domain.dto.request.ProjectCreationRequest;
import com.group4.DLS.domain.dto.request.ProjectUpdateRequest;
import com.group4.DLS.domain.dto.response.ProjectResponse;
import com.group4.DLS.domain.entity.Project;
import com.group4.DLS.domain.entity.ProjectMember;
import com.group4.DLS.domain.entity.User;
import com.group4.DLS.domain.entity.enums.ProjectStatus;
import com.group4.DLS.domain.entity.enums.UserRole;
import com.group4.DLS.domain.entity.enums.UserStatus;
import com.group4.DLS.exceptions.AppException;
import com.group4.DLS.exceptions.enums.ErrorCode;
import com.group4.DLS.mapper.ProjectMapper;
import com.group4.DLS.repository.ProjectMemberRepository;
import com.group4.DLS.repository.ProjectRepository;
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

    // ================= CREATE PROJECT =================
    public ProjectResponse createProject(ProjectCreationRequest request) {

        User manager = currentUserProvider.getCurrentUser();

        if (manager.getStatus() != UserStatus.ACTIVE) {
            throw new AppException(ErrorCode.USER_NOT_ACTIVE);
        }

        // if (manager.getUserRole() != UserRole.MANAGER) {
        //     throw new AppException(ErrorCode.FORBIDDEN);
        // }

        Project project = projectMapper.toProject(request);
        project = projectRepository.save(project);

        // Manager auto là member của project
        ProjectMember member = new ProjectMember();
        member.setProject(project);
        member.setUser(manager);
        projectMemberRepository.save(member);

        return projectMapper.toProjectResponse(project);
    }

    // ================= GET ALL PROJECT =================
    public List<ProjectResponse> getAllProjects() {

        User currentUser = currentUserProvider.getCurrentUser();

        if (currentUser.getStatus() != UserStatus.ACTIVE) {
            throw new AppException(ErrorCode.USER_NOT_ACTIVE);
        }

        // ADMIN: xem tất cả
        if (currentUser.getUserRole() == UserRole.ADMIN) {
            return projectRepository.findAll()
                    .stream()
                    .map(projectMapper::toProjectResponse)
                    .toList();
        }

        // MANAGER / ANNOTATOR / REVIEWER: chỉ xem project mình tham gia
        return projectMemberRepository.findByUser(currentUser)
                .stream()
                .map(ProjectMember::getProject)
                .map(projectMapper::toProjectResponse)
                .toList();
    }

    // ================= GET PROJECT BY ID =================
    public ProjectResponse getProjectById(String projectId) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new AppException(ErrorCode.PROJECT_NOT_FOUND));

        return projectMapper.toProjectResponse(project);
    }

    // ================= UPDATE PROJECT =================
    public ProjectResponse updateProject(String projectId, ProjectUpdateRequest request) {

        User currentUser = currentUserProvider.getCurrentUser();

        if (currentUser.getUserRole() != UserRole.MANAGER) {
            throw new AppException(ErrorCode.FORBIDDEN);
        }

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new AppException(ErrorCode.PROJECT_NOT_FOUND));

        projectMapper.updateProjectFromRequest(request, project);

        return projectMapper.toProjectResponse(
                projectRepository.save(project)
        );
    }

    // ================= DELETE PROJECT =================

    public void deleteProject(String projectId) {

        User currentUser = currentUserProvider.getCurrentUser();

        if (currentUser.getUserRole() != UserRole.MANAGER) {
            throw new AppException(ErrorCode.FORBIDDEN);
        }

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new AppException(ErrorCode.PROJECT_NOT_FOUND));

        project.setStatus(ProjectStatus.CANCELLED);
        projectRepository.save(project);
    }

}


