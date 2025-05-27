package com.java.bank_rest.dto.user;

import com.java.bank_rest.util.Role;
import com.java.bank_rest.util.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String username;
    private Role role;
    private UserStatus status;
}
