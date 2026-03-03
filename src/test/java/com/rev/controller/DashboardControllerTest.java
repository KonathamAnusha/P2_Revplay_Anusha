package com.rev.controller;

import com.rev.config.JwtUtil;
import com.rev.dto.UserStatsDTO;
import com.rev.entity.ArtistProfile;
import com.rev.entity.UserAccount;
import com.rev.entity.UserAccount.Role;
import com.rev.mapper.ArtistMapper;
import com.rev.repository.SongsRepository;
import com.rev.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DashboardController.class)
@AutoConfigureMockMvc(addFilters = false)
class DashboardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private ArtistServiceInterface artistService;
    @MockBean
    private ArtistMapper artistMapper;
    @MockBean
    private SongsServiceInterface songsService;
    @MockBean
    private AlbumServiceInterface albumService;
    @MockBean
    private UserStatsServiceInterface statsService;
    @MockBean
    private ListeningHistoryServiceInterface historyService;
    @MockBean
    private FavoriteServiceInterface favoriteService;
    @MockBean
    private PlaylistServiceInterface playlistService;
    @MockBean
    private ArtistAnalyticsService analyticsService;
    @MockBean
    private SongsRepository songsRepository;
    @MockBean
    private PodcastService podcastService;

    private UserAccount listenerUser;
    private UserAccount artistUser;
    private ArtistProfile artistProfile;
    private MockHttpSession session;

    @BeforeEach
    void setUp() {
        listenerUser = new UserAccount();
        listenerUser.setUserId(1L);
        listenerUser.setEmail("listener@example.com");
        listenerUser.setDisplayName("Listener User");
        listenerUser.setRole(Role.LISTENER);

        artistUser = new UserAccount();
        artistUser.setUserId(2L);
        artistUser.setEmail("artist@example.com");
        artistUser.setDisplayName("Artist User");
        artistUser.setRole(Role.ARTIST);

        artistProfile = new ArtistProfile();
        artistProfile.setArtistId(1L);

        session = new MockHttpSession();
    }

    @Test
    void showDashboard_RedirectToLogin_WhenNoUser() throws Exception {
        mockMvc.perform(get("/dashboard"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/auth/login"));
    }

    @Test
    void showDashboard_Listener() throws Exception {
        session.setAttribute("loggedUser", listenerUser);

        when(statsService.getUserStats(1L)).thenReturn(UserStatsDTO.builder().build());
        when(historyService.getRecentUserHistory(1L)).thenReturn(new ArrayList<>());
        when(favoriteService.getUserFavorites(1L)).thenReturn(new ArrayList<>());
        when(playlistService.getPlaylistsByUserId(1L)).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/dashboard").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/UserDashboard"))
                .andExpect(model().attributeExists("userStats", "recentlyPlayed", "favorites", "playlists"));
    }

    @Test
    void showDashboard_Artist() throws Exception {
        session.setAttribute("loggedUser", artistUser);
        when(artistService.getArtistProfileByUserId(2L)).thenReturn(artistProfile);
        when(songsService.getSongsByArtistPaginated(any(), any())).thenReturn(Page.empty());
        when(albumService.getAlbumsByArtistPaginated(any(), any())).thenReturn(Page.empty());

        mockMvc.perform(get("/dashboard").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/artist-dashboard"))
                .andExpect(model().attributeExists("artist"))
                .andExpect(model().attributeExists("mySongs"));
    }
}
