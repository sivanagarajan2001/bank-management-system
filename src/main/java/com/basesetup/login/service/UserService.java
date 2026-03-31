package com.basesetup.login.service;

import com.basesetup.login.dto.RegisterUserDto;
import com.basesetup.login.dto.UpdateUserRequestDto;
import com.basesetup.login.dto.UserResponse;
import com.basesetup.login.model.User;
import com.basesetup.login.model.constant.Role;
import com.basesetup.login.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<UserResponse> allUsers() {
        System.out.println();

        return userRepository.findAll()
                .stream()
                .map(this::getUserResponse)
                .toList();
    }

    public UserResponse getUserResponse(User user) {
        UserResponse response = new UserResponse();
        response.setEmail(user.getEmail());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setRole(user.getRole().name());
        return response;
    }

    public UserResponse updateUser(Long id, UpdateUserRequestDto updatedUser) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setFirstName(updatedUser.getFirstName());
        user.setLastName(updatedUser.getLastName());
        user.setRole(Role.valueOf(updatedUser.getRole().toUpperCase()));

        userRepository.save(user);

        return getUserResponse(user);
    }
}