package com.teleport.optimal_truck_load_planner.dto.requestDto;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ShipmentInfo {

    @NotBlank(message = "order id is required")
    private String id;

    @Min(value = 1, message = "payout_cents must be positive")
    @JsonProperty("payout_cents")
    private long payoutCents;

    @Min(value = 1, message = "weight_lbs must be positive")
    @JsonProperty("weight_lbs")
    private int weightLbs;

    @Min(value = 1, message = "volume_cuft must be positive")
    @JsonProperty("volume_cuft")
    private int volumeCuft;

    @NotBlank(message = "origin is required")
    @JsonProperty("origin")
    private String origin;

    @NotBlank(message = "destination is required")
    @JsonProperty("destination")
    private String destination;

    @NotNull(message = "pickup_date is required")
    @JsonProperty("pickup_date")
    private String pickupDate;

    @NotNull(message = "delivery_date is required")
    @JsonProperty("delivery_date")
    private String deliveryDate;

    @JsonProperty("is_hazmat")
    private boolean isHazmat;
}