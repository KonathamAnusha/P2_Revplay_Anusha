package com.rev.controller;

import com.rev.config.JwtUtil;
import com.rev.dto.UserDTO;
import com.rev.entity.UserAccount;
import com.rev.mapper.UserMapper;
import com.rev.service.UserServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthRestController {

    private final UserServiceInterface userService;
    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;

    // ---- REGISTER ----
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody UserDTO dto) {
        UserAccount saved = userService.registerUser(dto);
        String token = jwtUtil.generateToken(saved.getEmail(), saved.getRole().name());

        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("user", userMapper.toDTO(saved));
        return ResponseEntity.ok(response);
    }

    // ---- LOGIN ----
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> credentials) {
        String email = credentials.get("email");
        String password = credentials.get("password");

        UserAccount user = userService.login(email, password);
        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());

        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("user", userMapper.toDTO(user));
        return ResponseEntity.ok(response);
    }
}
