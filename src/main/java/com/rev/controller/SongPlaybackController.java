package com.rev.controller;

import com.rev.entity.UserAccount;
import com.rev.service.ListeningHistoryServiceInterface;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/playback")
@RequiredArgsConstructor
public class SongPlaybackController {

    private final ListeningHistoryServiceInterface historyService;

    @PostMapping("/start/{songId}")
    public String startPlayback(@PathVariable Long songId, HttpSession session) {
        UserAccount user = (UserAccount) session.getAttribute("loggedUser");
        if (user == null)
            return "User not logged in";

        historyService.addListeningHistory(user.getUserId(), songId);
        return "History recorded";
    }
}
