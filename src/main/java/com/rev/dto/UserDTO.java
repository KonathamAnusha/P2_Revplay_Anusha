package com.rev.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    private long userId;
    private String fullName;
    private String email;
    private String password;
    private String role; // LISTENER / ARTIST
    private String displayName;
    private String bio;
    private String profilePicture;
    private ArtistDTO artistProfile; // Optional for ARTIST role
}