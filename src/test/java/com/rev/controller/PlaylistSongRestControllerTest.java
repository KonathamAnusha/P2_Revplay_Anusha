package com.rev.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rev.config.JwtUtil;
import com.rev.dto.PlaylistSongDTO;
import com.rev.service.PlaylistSongServiceInterface;
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

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PlaylistSongRestController.class)
@AutoConfigureMockMvc(addFilters = false)
class PlaylistSongRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private PlaylistSongServiceInterface playlistSongService;

    @MockBean
    private SongsServiceInterface songsService;

    private PlaylistSongDTO testDTO;
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        testDTO = new PlaylistSongDTO();
        testDTO.setPlaylistId(1L);
        testDTO.setSongId(1L);
        testDTO.setOrderIndex(1);
    }

    @Test
    void addSong() throws Exception {
        when(playlistSongService.addSongToPlaylist(1L, 1L)).thenReturn(testDTO);

        mockMvc.perform(post("/api/playlist-songs/add")
                .param("playlistId", "1")
                .param("songId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderIndex").value(1));
    }

    @Test
    void removeSong() throws Exception {
        doNothing().when(playlistSongService).removeSongFromPlaylist(1L, 1L);

        mockMvc.perform(delete("/api/playlist-songs/remove")
                .param("playlistId", "1")
                .param("songId", "1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void getPlaylistSongs() throws Exception {
        when(playlistSongService.getSongsByPlaylist(1L)).thenReturn(Arrays.asList(testDTO));

        mockMvc.perform(get("/api/playlist-songs/playlist/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].playlistId").value(1));
    }

    @Test
    void reorderPlaylist() throws Exception {
        doNothing().when(playlistSongService).reorderPlaylist(eq(1L), anyList());

        mockMvc.perform(put("/api/playlist-songs/playlist/1/reorder")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Arrays.asList(1L, 2L))))
                .andExpect(status().isOk());
    }
}
