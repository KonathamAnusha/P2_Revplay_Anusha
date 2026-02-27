package com.rev.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FavoriteDTO {

    private Long favoriteId;
    private Long userId;
    private Long songId;
    private LocalDateTime createdAt;
}