package com.rev.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserStatsDTO {
    private int totalPlaylists;
    private int favoriteSongsCount;
    private long totalListeningTime; // in seconds
}