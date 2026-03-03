package com.rev.controller;

import com.rev.config.JwtUtil;
import com.rev.dto.UserStatsDTO;
import com.rev.entity.UserAccount;
import com.rev.entity.UserAccount.Role;
import com.rev.service.FavoriteServiceInterface;
import com.rev.service.ListeningHistoryServiceInterface;
import com.rev.service.SongsServiceInterface;
import com.rev.service.UserServiceInterface;
import com.rev.service.UserStatsServiceInterface;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private FavoriteServiceInterface favoriteService;
    @MockBean
    private ListeningHistoryServiceInterface historyService;
    @MockBean
    private UserStatsServiceInterface statsService;
    @MockBean
    private UserServiceInterface userService;
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
    void showFavorites() throws Exception {
        session.setAttribute("loggedUser", testUser);
        when(favoriteService.getUserFavorites(1L)).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/dashboard/favorites").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/favorites"))
                .andExpect(model().attributeExists("favoriteSongs"));
    }

    @Test
    void showHistory() throws Exception {
        session.setAttribute("loggedUser", testUser);
        when(historyService.getUserHistory(1L)).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/dashboard/history").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/history"))
                .andExpect(model().attributeExists("history"));
    }

    @Test
    void showProfile() throws Exception {
        session.setAttribute("loggedUser", testUser);
        when(statsService.getUserStats(1L)).thenReturn(UserStatsDTO.builder().build());

        mockMvc.perform(get("/dashboard/profile/1").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/Profile"))
                .andExpect(model().attributeExists("stats"));
    }

    @Test
    void clearHistory() throws Exception {
        session.setAttribute("loggedUser", testUser);
        mockMvc.perform(post("/dashboard/history/clear").session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/dashboard/history"));
    }
}
