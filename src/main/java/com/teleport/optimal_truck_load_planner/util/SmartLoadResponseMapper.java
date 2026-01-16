package com.teleport.optimal_truck_load_planner.util;

import com.teleport.optimal_truck_load_planner.dto.requestDto.ShipmentInfo;
import com.teleport.optimal_truck_load_planner.dto.requestDto.TruckInfo;
import com.teleport.optimal_truck_load_planner.dto.responseDto.SmartLoadResponseDto;
import com.teleport.optimal_truck_load_planner.model.OptimizerResult;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public final class SmartLoadResponseMapper {

    public static SmartLoadResponseDto toResponse(OptimizerResult optimizerResult, TruckInfo truck){

        int totalWeight = optimizerResult.getOrders()
                .stream()
                .mapToInt(ShipmentInfo::getWeightLbs)
                .sum();

        int totalVolume = optimizerResult.getOrders()
                .stream()
                .mapToInt(ShipmentInfo::getVolumeCuft)
                .sum();

        double weightUtil = totalWeight == 0 ? 0.0 :
                (totalWeight * 100.0) / truck.getMaxWeightLbs();

        double volumeUtil = totalVolume == 0 ? 0.0 :
                (totalVolume * 100.0) / truck.getMaxVolumeCuft();

        return SmartLoadResponseDto.builder()
                .truckId(truck.getId())
                .selectedOrderIds(
                        optimizerResult.getOrders()
                                .stream()
                                .map(ShipmentInfo::getId)
                                .collect(Collectors.toList())
                )
                .totalPayoutCents(optimizerResult.getTotalPayout())
                .totalWeightLbs(totalWeight)
                .totalVolumeCuft(totalVolume)
                .utilizationWeightPercent(round(weightUtil))
                .utilizationVolumePercent(round(volumeUtil))
                .build();
    }

    private static double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
