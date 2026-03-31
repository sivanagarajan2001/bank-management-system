package com.basesetup.login.controller;

import com.basesetup.login.dto.RegisterUserDto;
import com.basesetup.login.dto.UpdateUserRequestDto;
import com.basesetup.login.dto.UserResponse;
import com.basesetup.login.model.User;
import com.basesetup.login.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/manager")
@RequiredArgsConstructor
public class ManagerController {
    private final UserService userService;

    @GetMapping("/users")
    public List<UserResponse> getAllUsers() {
        return userService.allUsers();
    }
    @PutMapping("/user/{id}")
    public UserResponse updateUser(@PathVariable Long id,
                                   @RequestBody @Valid UpdateUserRequestDto user) {
        return userService.updateUser(id, user);
    }


}
