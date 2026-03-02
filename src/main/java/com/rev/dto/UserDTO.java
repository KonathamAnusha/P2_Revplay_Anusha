package com.rev.dto;

import com.rev.entity.UserAccount.Role;
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
    private Role role; // LISTENER / ARTIST
    private String displayName;
    private String bio;
    private String profilePicture;
    private ArtistDTO artistProfile;


//    // In UserDTO.java
//    private ArtistProfileDTO artistProfile = new ArtistProfileDTO();

//    @Data
//    public static class ArtistProfileDTO {
//        private String stageName;
//        private String genre;
//        private String bannerImage;
//        private String instagram;
//        private String twitter;
//        private String youtube;
//    }
}