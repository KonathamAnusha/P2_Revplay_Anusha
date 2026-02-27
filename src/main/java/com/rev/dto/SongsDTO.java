package com.rev.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SongsDTO {
    private Long songId;
    private String title;
    private String genre;
    private int duration;
    private String songUrl;
    private String language;
    private Long artistId;
    private Long albumId;
}