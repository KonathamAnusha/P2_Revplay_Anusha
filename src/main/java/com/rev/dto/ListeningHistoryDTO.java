package com.rev.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ListeningHistoryDTO {
    private Long historyId;
    private Long userId;
    private Long songId;
    private LocalDateTime playedAt;
    private String actionType; // e.g., PLAY
}