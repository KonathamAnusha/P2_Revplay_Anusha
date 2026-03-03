package com.rev.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor // Important for JPQL "new"
@ToString
@Builder
public class TopSongsDTO {

    private Long songId;
    private String title;
    private Long playCount;

}