package com.java.bank_rest.controller;

import com.java.bank_rest.dto.user.UserResponse;
import com.java.bank_rest.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @PatchMapping("/block/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse blockUser(@PathVariable Long userId) {
        return userService.blockUser(userId);
    }
}
