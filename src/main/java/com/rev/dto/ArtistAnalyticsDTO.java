package com.rev.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArtistAnalyticsDTO {
    private long totalFavorites;                // Total favorites for artist songs
    private long totalPlays;                    // Total plays for artist songs
    private List<TopSongDTO> topSongs;          // Top songs by play count
    private List<TopListenerDTO> topListeners;  // Top users who played artist songs the most

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class TopSongDTO {
        private Long songId;
        private String songTitle;
        private long playCount;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class TopListenerDTO {
        private Long userId;
        private String userName;
        private long playCount;
    }
}