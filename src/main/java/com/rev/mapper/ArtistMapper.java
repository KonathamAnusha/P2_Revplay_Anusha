package com.rev.mapper;

import com.rev.dto.ArtistDTO;
import com.rev.entity.ArtistProfile;
import com.rev.entity.UserAccount;
import org.springframework.stereotype.Component;

@Component
public class ArtistMapper {

    // ------------------- Convert DTO to Entity -------------------
    public ArtistProfile toEntity(ArtistDTO dto, UserAccount user) {
        if (dto == null) return null;

        return ArtistProfile.builder()
                .userAccount(user)
                .stageName(dto.getStageName())
                .genre(dto.getGenre())
                .bio(dto.getBio())
                .profilePic(dto.getProfilePic())
                .bannerImage(dto.getBannerImage())
                .instagram(dto.getInstagram())
                .twitter(dto.getTwitter())
                .youtube(dto.getYoutube())

                .build();
    }

    // ------------------- Convert Entity to DTO -------------------
    public ArtistDTO toDTO(ArtistProfile entity) {
        if (entity == null) return null;

        ArtistDTO dto = ArtistDTO.builder()
                .artistId(entity.getArtistId())
                .stageName(entity.getStageName())
                .genre(entity.getGenre())
                .bio(entity.getBio())
                .profilePic(entity.getProfilePic())
                .bannerImage(entity.getBannerImage())
                .instagram(entity.getInstagram())
                .twitter(entity.getTwitter())
                .youtube(entity.getYoutube())
                .build();

        if (entity.getUserAccount() != null) {
            dto.setUserId(entity.getUserAccount().getUserId());
            dto.setFullName(entity.getUserAccount().getFullName());
            dto.setEmail(entity.getUserAccount().getEmail());
            dto.setRole(entity.getUserAccount().getRole());
        }

        return dto;
    }
    // ------------------- Update existing Entity from DTO -------------------
    public void updateEntity(ArtistProfile entity, ArtistDTO dto) {
        if (entity == null || dto == null) return;

        if (dto.getStageName() != null) entity.setStageName(dto.getStageName());
        if (dto.getGenre() != null) entity.setGenre(dto.getGenre());
        if (dto.getBio() != null) entity.setBio(dto.getBio());
        if (dto.getProfilePic() != null) entity.setProfilePic(dto.getProfilePic());
        if (dto.getBannerImage() != null) entity.setBannerImage(dto.getBannerImage());
        if (dto.getInstagram() != null) entity.setInstagram(dto.getInstagram());
        if (dto.getTwitter() != null) entity.setTwitter(dto.getTwitter());
        if (dto.getYoutube() != null) entity.setYoutube(dto.getYoutube());

    }
}