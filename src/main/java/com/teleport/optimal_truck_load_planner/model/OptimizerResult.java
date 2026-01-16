package com.teleport.optimal_truck_load_planner.model;

import com.teleport.optimal_truck_load_planner.dto.requestDto.ShipmentInfo;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
@AllArgsConstructor
public class OptimizerResult {

    private List<ShipmentInfo> orders;
    private long totalPayout;

    public static OptimizerResult empty() {
        return new OptimizerResult(Collections.emptyList(), 0L);
    }
}
