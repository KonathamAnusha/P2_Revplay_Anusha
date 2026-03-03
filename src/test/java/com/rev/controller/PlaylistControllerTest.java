package com.rev.controller;

import com.rev.config.JwtUtil;
import com.rev.dto.PlaylistDTO;
import com.rev.entity.UserAccount;
import com.rev.entity.UserAccount.Role;
import com.rev.service.PlaylistServiceInterface;
import com.rev.service.SongsServiceInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PlaylistController.class)
@AutoConfigureMockMvc(addFilters = false)
class PlaylistControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private PlaylistServiceInterface playlistService;

    @MockBean
    private SongsServiceInterface songsService;

    private UserAccount testUser;
    private MockHttpSession session;

    @BeforeEach
    void setUp() {
        testUser = new UserAccount();
        testUser.setUserId(1L);
        testUser.setRole(Role.LISTENER);
        session = new MockHttpSession();
    }

    @Test
    void listPlaylists() throws Exception {
        session.setAttribute("loggedUser", testUser);
        when(playlistService.getPlaylistsByUserId(1L)).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/dashboard/playlists").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/my-playlists"))
                .andExpect(model().attributeExists("playlists"));
    }

    @Test
    void viewPlaylist() throws Exception {
        session.setAttribute("loggedUser", testUser);
        when(playlistService.getPlaylistById(1L)).thenReturn(PlaylistDTO.builder().playlistId(1L).build());

        mockMvc.perform(get("/dashboard/playlists/1").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/playlist-detail"))
                .andExpect(model().attributeExists("playlist"));
    }

    @Test
    void createPlaylist() throws Exception {
        session.setAttribute("loggedUser", testUser);
        mockMvc.perform(post("/dashboard/playlists/create").session(session)
                .param("name", "New Playlist")
                .param("privacy", "PUBLIC"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/dashboard/playlists"));
    }

    @Test
    void deletePlaylist() throws Exception {
        session.setAttribute("loggedUser", testUser);
        mockMvc.perform(post("/dashboard/playlists/delete/1").session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/dashboard/playlists"));
    }
}
