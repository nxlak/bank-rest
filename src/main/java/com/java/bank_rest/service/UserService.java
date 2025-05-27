package com.java.bank_rest.service;

import com.java.bank_rest.dto.user.UserResponse;
import com.java.bank_rest.entity.User;
import com.java.bank_rest.repository.UserRepo;
import com.java.bank_rest.util.UserStatus;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
        );
    }

    public UserResponse blockUser(Long userId) {
        User user = userRepo.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));
        user.setStatus(UserStatus.BLOCKED);
        userRepo.save(user);

        return UserResponse.builder()
                .id(userId)
                .username(user.getUsername())
                .role(user.getRole())
                .status(user.getStatus())
                .build();
    }

}
