package com.rev.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlbumDTO {
    private Long albumId;
    private String name;
    private String description;
    private String coverArt;
    private Long artistId;
    private LocalDate releaseDate; // official release date
    private java.util.List<Long> songIds;
}