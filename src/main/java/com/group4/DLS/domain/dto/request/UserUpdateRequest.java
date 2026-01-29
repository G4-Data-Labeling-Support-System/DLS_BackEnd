package com.group4.DLS.domain.dto.request;

import com.group4.DLS.domain.entity.enums.UserRole;
import com.group4.DLS.domain.entity.enums.UserStatus;

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
public class UserUpdateRequest {
    String username;
    String fullName;
    String email;
    UserRole userRole;
    String coverImage;
    UserStatus status;
}
