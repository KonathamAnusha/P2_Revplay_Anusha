package com.rev.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rev.config.JwtUtil;
import com.rev.dto.UserDTO;
import com.rev.entity.UserAccount;
import com.rev.mapper.UserMapper;
import com.rev.service.UserServiceInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthRestController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private UserServiceInterface userService;

    @MockBean
    private UserMapper userMapper;

    private UserAccount testUser;
    private UserDTO testDTO;
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        testUser = new UserAccount();
        testUser.setEmail("test@example.com");
        testUser.setRole(UserAccount.Role.LISTENER);

        testDTO = new UserDTO();
        testDTO.setEmail("test@example.com");
    }

    @Test
    void register() throws Exception {
        when(userService.registerUser(any(UserDTO.class))).thenReturn(testUser);
        when(jwtUtil.generateToken(anyString(), anyString())).thenReturn("test-token");
        when(userMapper.toDTO(testUser)).thenReturn(testDTO);

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("test-token"))
                .andExpect(jsonPath("$.user.email").value("test@example.com"));
    }

    @Test
    void login() throws Exception {
        Map<String, String> credentials = new HashMap<>();
        credentials.put("email", "test@example.com");
        credentials.put("password", "password");

        when(userService.login("test@example.com", "password")).thenReturn(testUser);
        when(jwtUtil.generateToken("test@example.com", "LISTENER")).thenReturn("test-token");
        when(userMapper.toDTO(testUser)).thenReturn(testDTO);

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(credentials)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("test-token"));
    }
}
