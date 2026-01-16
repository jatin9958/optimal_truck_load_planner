package com.teleport.optimal_truck_load_planner.dto.responseDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SmartLoadResponseDto {

    @JsonProperty("truck_id")
    private String truckId;
    @JsonProperty("selected_order_ids")
    private List<String> selectedOrderIds;
    @JsonProperty("total_payout_cents")
    private long totalPayoutCents;
    @JsonProperty("total_weight_lbs")
    private int totalWeightLbs;
    @JsonProperty("total_volume_cuft")
    private int totalVolumeCuft;
    @JsonProperty("utilization_weight_percent")
    private double utilizationWeightPercent;
    @JsonProperty("utilization_volume_percent")
    private double utilizationVolumePercent;
}
