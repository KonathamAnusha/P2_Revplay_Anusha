package com.rev.dto;

import lombok.*;

import java.util.List;
import com.rev.dto.TopSongsDTO;
import com.rev.dto.TopListenerDTO;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArtistAnalyticsDTO {
    private long totalFavorites;                // Total favorites for artist songs
    private long totalPlays;                    // Total plays for artist songs
    private List<TopSongsDTO> topSongs;          // Top songs by play count
    private List<TopListenerDTO> topListeners;  // Top users who played artist songs the most




}