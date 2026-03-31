package com.basesetup.login.service;

import com.basesetup.login.dto.LoginRequestDto;
import com.basesetup.login.dto.RegisterUserDto;
import com.basesetup.login.exception.ApplicationException;
import com.basesetup.login.model.User;
import com.basesetup.login.model.constant.Role;
import com.basesetup.login.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public String signup(RegisterUserDto input) {
        if (userRepository.existsByEmail(input.getEmail()))
            throw new ApplicationException("email id already exists");
        User user = new User();
        user.setUsername(input.getEmail());
        user.setFirstName(input.getFirstName());
        user.setLastName(input.getLastName());
        user.setEmail(input.getEmail());
        user.setPassword(passwordEncoder.encode(input.getPassword()));
        user.setCreatedAt(LocalDateTime.now());
        user.setRole(Role.valueOf(input.getRole().toUpperCase()));
        user.setCreatedBy(input.getEmail()); // need to do - implement the UserContextHolder
        userRepository.save(user);
        return "Register successful";
    }

    public User authenticate(LoginRequestDto input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getUsername(),
                        input.getPassword()
                )
        );
        return userRepository.findByEmail(input.getUsername())
                .orElseThrow();
    }
}
