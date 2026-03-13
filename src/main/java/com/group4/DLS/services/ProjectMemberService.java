package com.group4.DLS.services;

import com.group4.DLS.domain.dto.response.ProjectMemberResponse;
import com.group4.DLS.domain.entity.Project;
import com.group4.DLS.domain.entity.ProjectMember;
import com.group4.DLS.domain.entity.User;
import com.group4.DLS.exceptions.AppException;
import com.group4.DLS.exceptions.enums.ErrorCode;
import com.group4.DLS.mappers.ProjectMemberMapper;
import com.group4.DLS.repositories.ProjectMemberRepository;
import com.group4.DLS.repositories.ProjectRepository;
import com.group4.DLS.repositories.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProjectMemberService {

    ProjectMemberRepository projectMemberRepository;
    ProjectRepository projectRepository;
    UserRepository userRepository;
    ProjectMemberMapper projectMemberMapper;

    //List all members of a project
    public List<ProjectMemberResponse> getMembersByProjectId(String projectId) {
        List<ProjectMember> members = projectMemberRepository.findByProject_ProjectId(projectId);
        if (members.isEmpty()) {
            throw new AppException(ErrorCode.PROJECT_MEMBER_NOT_FOUND);
        }
        return projectMemberMapper.toProjectMemberResponse(members);
    }

    //assign member to project
    public ProjectMember assignMemberToProject(String projectId, String userId) {
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new AppException(ErrorCode.PROJECT_NOT_FOUND));
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        ProjectMember projectMember = new ProjectMember();
        projectMember.setProject(project);
        projectMember.setUser(user);
        projectMember.setJoinAt(LocalDateTime.now());
        return projectMemberRepository.save(projectMember);
    }
}
