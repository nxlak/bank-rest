package com.java.bank_rest.dto;

import lombok.Getter;

@Getter
public class AuthRequest {
    private String username;
    private String password;
}
