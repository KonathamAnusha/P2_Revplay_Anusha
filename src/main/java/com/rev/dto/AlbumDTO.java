package com.rev.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class AlbumDTO {
    private Long albumId;
    private String name;
    private String description;
    private String coverArt;
    private Long artistId;
    private LocalDate releaseDate;  // official release date
}