package com.rev.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArtistDTO {
    private long artistId;
    private String stageName;
    private String genre;
    private String bio;
    private String profilePic;
    private String bannerImage;
    private String instagram;
    private String twitter;
    private String youtube;

    // Add User info
    private Long userId;
    private String fullName;
    private String email;
    private String role;
}