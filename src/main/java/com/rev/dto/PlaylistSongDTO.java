package com.rev.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlaylistSongDTO {
    private Long id;
    private Long playlistId;
    private Long songId;
    private int orderIndex;
}