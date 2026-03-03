package com.rev.controller;

import com.rev.config.JwtUtil;
import com.rev.entity.UserAccount;
import com.rev.entity.UserAccount.Role;
import com.rev.service.SongsServiceInterface;
import com.rev.service.AlbumServiceInterface;
import com.rev.service.ArtistServiceInterface;
import com.rev.mapper.ArtistMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BrowseController.class)
@AutoConfigureMockMvc(addFilters = false)
class BrowseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private SongsServiceInterface songsService;

    @MockBean
    private AlbumServiceInterface albumService;

    @MockBean
    private ArtistServiceInterface artistService;

    @MockBean
    private ArtistMapper artistMapper;

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
    void browse_AllSongs() throws Exception {
        session.setAttribute("loggedUser", testUser);
        when(songsService.getAllSongs()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/dashboard/browse").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/browse"))
                .andExpect(model().attributeExists("songs"));
    }

    @Test
    void browse_WithSearch() throws Exception {
        session.setAttribute("loggedUser", testUser);
        when(songsService.searchSongsByTitle("Test")).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/dashboard/browse").session(session)
                .param("search", "Test"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("searchQuery", "Test"));
    }
}
