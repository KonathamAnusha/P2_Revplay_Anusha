package com.rev.controller;

import com.rev.config.JwtUtil;
import com.rev.dto.ListeningHistoryDTO;
import com.rev.service.ListeningHistoryServiceInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ListeningHistoryRestController.class)
@AutoConfigureMockMvc(addFilters = false)
class ListeningHistoryRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private ListeningHistoryServiceInterface historyService;

    private ListeningHistoryDTO testDTO;

    @BeforeEach
    void setUp() {
        testDTO = new ListeningHistoryDTO();
        testDTO.setUserId(1L);
        testDTO.setSongId(1L);
    }

    @Test
    void addHistory() throws Exception {
        when(historyService.addListeningHistory(1L, 1L)).thenReturn(testDTO);

        mockMvc.perform(post("/api/history")
                .param("userId", "1")
                .param("songId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1));
    }

    @Test
    void getUserHistory() throws Exception {
        when(historyService.getUserHistory(1L)).thenReturn(Arrays.asList(testDTO));

        mockMvc.perform(get("/api/history/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userId").value(1));
    }

    @Test
    void clearUserHistory() throws Exception {
        mockMvc.perform(delete("/api/history/user/1"))
                .andExpect(status().isNoContent());
    }
}
