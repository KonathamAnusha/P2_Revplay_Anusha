package com.rev.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rev.config.JwtUtil;
import com.rev.dto.PlaylistDTO;
import com.rev.service.PlaylistServiceInterface;
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

@WebMvcTest(PlaylistRestController.class)
@AutoConfigureMockMvc(addFilters = false)
class PlaylistRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private PlaylistServiceInterface playlistService;

    @MockBean
    private SongsServiceInterface songsService;

    private PlaylistDTO testDTO;
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        testDTO = PlaylistDTO.builder()
                .playlistId(1L)
                .name("My Playlist")
                .privacy("PUBLIC")
                .userId(1L)
                .build();
    }

    @Test
    void createPlaylist() throws Exception {
        when(playlistService.createPlaylist(any(PlaylistDTO.class))).thenReturn(testDTO);

        mockMvc.perform(post("/api/playlists")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("My Playlist"));
    }

    @Test
    void getPlaylistById() throws Exception {
        when(playlistService.getPlaylistById(1L)).thenReturn(testDTO);

        mockMvc.perform(get("/api/playlists/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.playlistId").value(1));
    }

    @Test
    void getPlaylistsByUserId() throws Exception {
        when(playlistService.getPlaylistsByUserId(1L)).thenReturn(Arrays.asList(testDTO));

        mockMvc.perform(get("/api/playlists/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("My Playlist"));
    }

    @Test
    void deletePlaylist() throws Exception {
        mockMvc.perform(delete("/api/playlists/1"))
                .andExpect(status().isOk());
    }
}
