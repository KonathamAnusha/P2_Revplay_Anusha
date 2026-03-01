package com.rev.service;

import com.rev.dto.UserStatsDTO;

public interface UserStatsServiceInterface {
    UserStatsDTO getUserStats(Long userId);
}