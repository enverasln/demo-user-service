package com.example.userservice.service;

import com.example.userservice.dto.UserRequest;
import com.example.userservice.dto.UserResponse;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * In-memory user store. A real implementation would persist to a DB.
 */
@Service
public class UserService {

    private final ConcurrentHashMap<String, Long> users = new ConcurrentHashMap<>();
    private final AtomicLong idSeq = new AtomicLong();

    public UserResponse register(UserRequest request) {
        long id = users.computeIfAbsent(request.email(), e -> idSeq.incrementAndGet());
        return new UserResponse(id, request.email());
    }
}
