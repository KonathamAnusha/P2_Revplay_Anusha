package com.rev.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rev.config.JwtUtil;
import com.rev.dto.SongsDTO;
import com.rev.service.SongsServiceInterface;
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

@WebMvcTest(SongsRestController.class)
@AutoConfigureMockMvc(addFilters = false)
class SongsRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private SongsServiceInterface songsService;

    private SongsDTO testDTO;
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        testDTO = SongsDTO.builder()
                .songId(1L)
                .title("Test Song")
                .genre("Pop")
                .artistId(1L)
                .build();
    }

    @Test
    void createSong() throws Exception {
        when(songsService.createSong(any(SongsDTO.class))).thenReturn(testDTO);

        mockMvc.perform(post("/api/songs")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Song"));
    }

    @Test
    void getAllSongs() throws Exception {
        when(songsService.getAllSongs()).thenReturn(Arrays.asList(testDTO));

        mockMvc.perform(get("/api/songs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Test Song"));
    }

    @Test
    void getSongById() throws Exception {
        when(songsService.getSongById(1L)).thenReturn(testDTO);

        mockMvc.perform(get("/api/songs/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.songId").value(1));
    }

    @Test
    void searchSongs() throws Exception {
        when(songsService.searchSongsByTitle("Test")).thenReturn(Arrays.asList(testDTO));

        mockMvc.perform(get("/api/songs/search?title=Test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Test Song"));
    }

    @Test
    void playSong() throws Exception {
        mockMvc.perform(post("/api/songs/1/play"))
                .andExpect(status().isOk());
    }
}
