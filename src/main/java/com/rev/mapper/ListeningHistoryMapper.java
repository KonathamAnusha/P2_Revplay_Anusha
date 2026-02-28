package com.rev.mapper;

import com.rev.dto.ListeningHistoryDTO;
import com.rev.entity.ListeningHistory;
import org.springframework.stereotype.Component;

@Component
public class ListeningHistoryMapper {

    public ListeningHistoryDTO toDTO(ListeningHistory history) {
        return ListeningHistoryDTO.builder()
                .historyId(history.getHistoryId())
                .userId(history.getUser().getUserId())
                .songId(history.getSong().getSongId())
                .playedAt(history.getPlayedAt())
                .actionType(history.getActionType().name())
                .build();
    }
}