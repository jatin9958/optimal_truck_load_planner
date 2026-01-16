package com.teleport.optimal_truck_load_planner.dto.requestDto;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TruckInfo {

    @NotBlank(message = "truck id is required")
    private String id;

    @Min(value = 1, message = "max_weight_lbs must be positive")
    @JsonProperty("max_weight_lbs")
    private int maxWeightLbs;

    @Min(value = 1, message = "max_volume_cuft must be positive")
    @JsonProperty("max_volume_cuft")
    private int maxVolumeCuft;

}