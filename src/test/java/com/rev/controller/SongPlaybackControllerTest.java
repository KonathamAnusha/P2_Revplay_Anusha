package com.rev.controller;

import com.rev.config.JwtUtil;
import com.rev.entity.UserAccount;
import com.rev.service.ListeningHistoryServiceInterface;
import com.rev.service.SongsServiceInterface;
import com.rev.repository.SongsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SongPlaybackController.class)
@AutoConfigureMockMvc(addFilters = false)
class SongPlaybackControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private ListeningHistoryServiceInterface historyService;

    @MockBean
    private SongsServiceInterface songsService;

    @MockBean
    private SongsRepository songsRepository;

    private UserAccount testUser;
    private MockHttpSession session;

    @BeforeEach
    void setUp() {
        testUser = new UserAccount();
        testUser.setUserId(1L);
        session = new MockHttpSession();
    }

    @Test
    void startPlayback_Success() throws Exception {
        session.setAttribute("loggedUser", testUser);
        mockMvc.perform(post("/api/playback/start/1").session(session))
                .andExpect(status().isOk())
                .andExpect(content().string("History recorded"));
    }

    @Test
    void startPlayback_Unauthorized() throws Exception {
        mockMvc.perform(post("/api/playback/start/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("User not logged in"));
    }
}
