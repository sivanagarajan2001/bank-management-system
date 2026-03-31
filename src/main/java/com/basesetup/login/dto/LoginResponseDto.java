package com.basesetup.login.dto;

import lombok.Data;

@Data
public class LoginResponseDto {
    private long expiresIn;
    private String token;
}
