package com.rev.mapper;

import com.rev.dto.FavoriteDTO;
import com.rev.entity.Favorite;
import org.springframework.stereotype.Component;

@Component
public class FavoriteMapper {

    // Convert Favorite entity → DTO
    public FavoriteDTO toDTO(Favorite fav) {
        return FavoriteDTO.builder()
                .favoriteId(fav.getFavoriteId())
                .userId(fav.getUser().getUserId())
                .songId(fav.getSong().getSongId())
                .createdAt(fav.getCreatedAt())
                .build();
    }
}