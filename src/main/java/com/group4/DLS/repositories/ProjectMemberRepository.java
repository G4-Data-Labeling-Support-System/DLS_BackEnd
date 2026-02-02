package com.group4.DLS.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.group4.DLS.domain.entity.ProjectMember;
import com.group4.DLS.domain.entity.User;

import java.util.List;

public interface ProjectMemberRepository
        extends JpaRepository<ProjectMember, String> {
    List<ProjectMember> findByUser(User user);
}
