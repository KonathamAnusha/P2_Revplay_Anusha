package com.rev.mapper;

import com.rev.dto.UserDTO;
import com.rev.entity.ArtistProfile;
import com.rev.entity.UserAccount;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    private final ArtistMapper artistMapper;

    public UserMapper(ArtistMapper artistMapper) {
        this.artistMapper = artistMapper;
    }

    // ========================
    // DTO -> ENTITY
    // ========================
    public UserAccount toEntity(UserDTO dto) {
        if (dto == null) return null;

        UserAccount entity = new UserAccount();

        entity.setFullName(dto.getFullName());
        entity.setEmail(dto.getEmail());
        entity.setPasswordHash(dto.getPassword()); // password encoded in service
        entity.setDisplayName(dto.getDisplayName());
        entity.setBio(dto.getBio());
        entity.setProfilePicture(dto.getProfilePicture());

        // Convert String -> ENUM
        if (dto.getRole() != null) {
            entity.setRole(UserAccount.Role.valueOf(dto.getRole().toUpperCase()));
        }

        // Handle Artist Profile only if role is ARTIST
        if (entity.getRole() == UserAccount.Role.ARTIST && dto.getArtistProfile() != null) {
            ArtistProfile artist = artistMapper.toEntity(dto.getArtistProfile(), entity);
            entity.setArtistProfile(artist);
        }

        return entity;
    }

    // ========================
    // ENTITY -> DTO
    // ========================
    public UserDTO toDTO(UserAccount entity) {
        if (entity == null) return null;

        UserDTO dto = UserDTO.builder()
                .userId(entity.getUserId())
                .fullName(entity.getFullName())
                .email(entity.getEmail())
                .displayName(entity.getDisplayName())
                .bio(entity.getBio())
                .profilePicture(entity.getProfilePicture())
                // Convert ENUM -> String
                .role(entity.getRole() != null ? entity.getRole().name() : null)
                .build();

        if (entity.getArtistProfile() != null) {
            dto.setArtistProfile(
                    artistMapper.toDTO(entity.getArtistProfile())
            );
        }

        return dto;
    }

    // ========================
    // UPDATE EXISTING ENTITY
    // ========================
    public void updateEntity(UserAccount entity, UserDTO dto) {
        if (entity == null || dto == null) return;

        if (dto.getFullName() != null)
            entity.setFullName(dto.getFullName());

        if (dto.getEmail() != null)
            entity.setEmail(dto.getEmail());

        if (dto.getPassword() != null)
            entity.setPasswordHash(dto.getPassword());

        // Convert String -> ENUM
        if (dto.getRole() != null) {
            entity.setRole(UserAccount.Role.valueOf(dto.getRole().toUpperCase()));
        }

        if (dto.getDisplayName() != null)
            entity.setDisplayName(dto.getDisplayName());

        if (dto.getBio() != null)
            entity.setBio(dto.getBio());

        if (dto.getProfilePicture() != null)
            entity.setProfilePicture(dto.getProfilePicture());

        // Artist profile handling
        if (entity.getRole() == UserAccount.Role.ARTIST && dto.getArtistProfile() != null) {

            if (entity.getArtistProfile() == null) {
                entity.setArtistProfile(
                        artistMapper.toEntity(dto.getArtistProfile(), entity)
                );
            } else {
                artistMapper.updateEntity(
                        entity.getArtistProfile(),
                        dto.getArtistProfile()
                );
            }
        }
    }
}