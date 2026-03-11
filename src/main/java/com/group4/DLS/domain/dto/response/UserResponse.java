package com.group4.DLS.domain.dto.response;

import java.time.LocalDate;

import com.group4.DLS.domain.enums.UserRole;
import com.group4.DLS.domain.enums.UserStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class UserResponse {
    String userId;
    String username;
    String email;
    String coverImage;
    String specialization;
    UserRole role;
    UserStatus userStatus;
    LocalDate createdAt;
}
