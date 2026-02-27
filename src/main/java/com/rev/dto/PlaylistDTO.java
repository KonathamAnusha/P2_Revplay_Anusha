package com.rev.dto;

import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlaylistDTO {

    private Long playlistId;
    private String name;
    private String description;
    private String privacy; // PUBLIC / PRIVATE
    private Long userId; // ID of the owner
    private List<Long> songIds; // List of song IDs in the playlist
    private LocalDateTime createdAt;
}