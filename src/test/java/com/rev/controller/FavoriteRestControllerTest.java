package com.rev.controller;

import com.rev.config.JwtUtil;
import com.rev.dto.FavoriteDTO;
import com.rev.service.FavoriteServiceInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FavoriteRestController.class)
@AutoConfigureMockMvc(addFilters = false)
class FavoriteRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private FavoriteServiceInterface favoriteService;

    private FavoriteDTO testDTO;

    @BeforeEach
    void setUp() {
        testDTO = new FavoriteDTO();
        testDTO.setUserId(1L);
        testDTO.setSongId(1L);
    }

    @Test
    void addFavorite() throws Exception {
        when(favoriteService.addFavorite(1L, 1L)).thenReturn(testDTO);

        mockMvc.perform(post("/api/favorites")
                .param("userId", "1")
                .param("songId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1));
    }

    @Test
    void getUserFavorites() throws Exception {
        when(favoriteService.getUserFavorites(1L)).thenReturn(Arrays.asList(testDTO));

        mockMvc.perform(get("/api/favorites/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userId").value(1));
    }

    @Test
    void removeFavorite() throws Exception {
        mockMvc.perform(delete("/api/favorites")
                .param("userId", "1")
                .param("songId", "1"))
                .andExpect(status().isOk());
    }
}
