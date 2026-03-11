package com.group4.DLS.domain.dto.request;

import com.group4.DLS.domain.enums.UserRole;
import com.group4.DLS.domain.enums.UserStatus;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
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
    @Size(min = 3, max = 50, message = "INVALID_USERNAME_LENGTH")
    String username;

    @Email(message = "INVALID_EMAIL_FORMAT")
    String email;
    
    @Size(min = 8, message = "INVALID_PASSWORD_LENGTH")
    String password;

    String coverImage;

    String specialization;
    
    UserRole role;

    UserStatus userStatus;
}
