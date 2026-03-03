package com.rev.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rev.config.JwtUtil;
import com.rev.dto.AlbumDTO;
import com.rev.dto.SongsDTO;
import com.rev.service.AlbumServiceInterface;
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

@WebMvcTest(AlbumRestController.class)
@AutoConfigureMockMvc(addFilters = false)
class AlbumRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private AlbumServiceInterface albumService;

    @MockBean
    private SongsServiceInterface songsService;

    private AlbumDTO testDTO;
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        testDTO = AlbumDTO.builder()
                .albumId(1L)
                .name("Test Album")
                .artistId(1L)
                .build();
    }

    @Test
    void createAlbum() throws Exception {
        when(albumService.createAlbum(eq(1L), any(AlbumDTO.class))).thenReturn(testDTO);

        mockMvc.perform(post("/api/albums/artists/1/albums")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Album"));
    }

    @Test
    void getAlbumById() throws Exception {
        when(albumService.getAlbumById(1L)).thenReturn(testDTO);

        mockMvc.perform(get("/api/albums/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.albumId").value(1));
    }

    @Test
    void getAllAlbums() throws Exception {
        when(albumService.getAllAlbums()).thenReturn(Arrays.asList(testDTO));

        mockMvc.perform(get("/api/albums"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Test Album"));
    }

    @Test
    void addSongToAlbum() throws Exception {
        mockMvc.perform(post("/api/albums/1/songs/1"))
                .andExpect(status().isOk());
    }
}
