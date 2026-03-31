package com.basesetup.login.config;

import com.basesetup.login.dto.UserContextDto;

public class UserContextHolder {
    private static final ThreadLocal<UserContextDto> USER_CONTEXT = new ThreadLocal<>();

    public static UserContextDto getUserContext() {
        return USER_CONTEXT.get();
    }

    public static void setUserContext(UserContextDto userContextDto) {
        USER_CONTEXT.set(userContextDto);
    }

    public static void clear() {
        USER_CONTEXT.remove();
    }
}