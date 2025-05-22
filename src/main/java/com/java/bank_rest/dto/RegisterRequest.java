package com.java.bank_rest.dto;

import com.java.bank_rest.util.Role;
import lombok.Getter;

@Getter
public class RegisterRequest {
    private String username;
    private String password;
    private Role role;
}
