package com.rev.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SongsDTO {
    private Long songId;
    private String title;
    private String genre;
    private int duration;
    private String songUrl;
    private String language;
    private Boolean isPublic;
    private Long artistId;
    private Long albumId;

    // Extra display fields
    private String artistName;
    private String albumName;
    private String coverArt;
    private LocalDateTime createdAt;
    private Long playCount;
}