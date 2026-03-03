package com.rev.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TopListenerDTO {
    private Long userId;
    private String userName;
    private Long playCount;
}