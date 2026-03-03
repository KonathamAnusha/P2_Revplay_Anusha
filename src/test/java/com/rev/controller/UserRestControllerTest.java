package com.rev.controller;

import com.rev.config.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rev.dto.UserDTO;
import com.rev.dto.UserStatsDTO;
import com.rev.entity.UserAccount;
import com.rev.mapper.UserMapper;
import com.rev.service.UserServiceInterface;
import com.rev.service.UserStatsServiceInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserRestController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private UserServiceInterface userService;

    @MockBean
    private UserMapper userMapper;

    @MockBean
    private UserStatsServiceInterface userStatsService;

    private UserAccount testUser;
    private UserDTO testDTO;
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        testUser = new UserAccount();
        testUser.setUserId(1L);
        testUser.setEmail("test@example.com");

        testDTO = new UserDTO();
        testDTO.setUserId(1L);
        testDTO.setEmail("test@example.com");
    }

    @Test
    void registerUser() throws Exception {
        when(userService.registerUser(any(UserDTO.class))).thenReturn(testUser);
        when(userMapper.toDTO(testUser)).thenReturn(testDTO);

        mockMvc.perform(post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    void getAllUsers() throws Exception {
        when(userService.getAllUsers()).thenReturn(Arrays.asList(testUser));
        when(userMapper.toDTO(testUser)).thenReturn(testDTO);

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value("test@example.com"));
    }

    @Test
    void getUserById() throws Exception {
        when(userService.getUserById(1L)).thenReturn(testUser);
        when(userMapper.toDTO(testUser)).thenReturn(testDTO);

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1));
    }

    @Test
    void updateUser() throws Exception {
        when(userService.updateUser(eq(1L), any(UserDTO.class))).thenReturn(testUser);
        when(userMapper.toDTO(testUser)).thenReturn(testDTO);

        mockMvc.perform(put("/api/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testDTO)))
                .andExpect(status().isOk());
    }

    @Test
    void deleteUserById() throws Exception {
        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("User deleted successfully"));
    }

    @Test
    void getUserStats() throws Exception {
        UserStatsDTO stats = UserStatsDTO.builder().favoriteSongsCount(10).build();
        when(userStatsService.getUserStats(1L)).thenReturn(stats);

        mockMvc.perform(get("/api/users/1/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.favoriteSongsCount").value(10));
    }
}
