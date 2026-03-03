package com.rev.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SongViewDTO {
    private Long songId;
    private String title;
    private String artistName;
    private String coverImage;
    private String audioUrl;
}
