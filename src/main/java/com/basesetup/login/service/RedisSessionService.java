package com.basesetup.login.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class RedisSessionService {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Value("${session.ttl}")
    private long ttl;

    public String createSession(String username) {
        String sessionId = UUID.randomUUID().toString();
        redisTemplate.opsForValue().set(username, sessionId, ttl, TimeUnit.MILLISECONDS);
        return sessionId;
    }

    public boolean validateSession(String username, String sessionId) {
        String storedSession = redisTemplate.opsForValue().get(username);
        return sessionId != null && sessionId.equals(storedSession);
    }
}
