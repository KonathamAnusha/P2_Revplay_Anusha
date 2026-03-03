package com.rev.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rev.config.JwtUtil;
import com.rev.dto.ArtistAnalyticsDTO;
import com.rev.dto.ArtistDTO;
import com.rev.entity.ArtistProfile;
import com.rev.mapper.ArtistMapper;
import com.rev.service.ArtistAnalyticsService;
import com.rev.service.ArtistServiceInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.HashMap;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ArtistRestController.class)
@AutoConfigureMockMvc(addFilters = false)
class ArtistRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private ArtistServiceInterface artistService;

    @MockBean
    private ArtistMapper artistMapper;

    @MockBean
    private ArtistAnalyticsService artistAnalyticsService;

    private ArtistProfile testProfile;
    private ArtistDTO testDTO;
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        testProfile = new ArtistProfile();
        testProfile.setArtistId(1L);
        testProfile.setStageName("TestArtist");

        testDTO = new ArtistDTO();
        testDTO.setStageName("TestArtist");
    }

    @Test
    void getArtistProfileById() throws Exception {
        when(artistService.getArtistProfileById(1L)).thenReturn(testProfile);
        when(artistMapper.toDTO(testProfile)).thenReturn(testDTO);

        mockMvc.perform(get("/api/artists/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stageName").value("TestArtist"));
    }

    @Test
    void getAllArtists() throws Exception {
        when(artistService.getAllArtists()).thenReturn(Arrays.asList(testProfile));
        when(artistMapper.toDTO(testProfile)).thenReturn(testDTO);

        mockMvc.perform(get("/api/artists"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].stageName").value("TestArtist"));
    }

    @Test
    void getAnalytics() throws Exception {
        ArtistAnalyticsDTO analytics = ArtistAnalyticsDTO.builder().totalPlays(100L).build();
        when(artistAnalyticsService.getArtistAnalytics(1L)).thenReturn(analytics);

        mockMvc.perform(get("/api/artists/1/analytics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPlays").value(100));
    }
}
