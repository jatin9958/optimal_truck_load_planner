package com.teleport.optimal_truck_load_planner.service;

import com.teleport.optimal_truck_load_planner.dto.requestDto.SmartLoadRequestDto;
import com.teleport.optimal_truck_load_planner.dto.responseDto.SmartLoadResponseDto;

public interface SmartLoadPlannerService {
    SmartLoadResponseDto optimizeLoad(SmartLoadRequestDto smartLoadRequestDto);
}
