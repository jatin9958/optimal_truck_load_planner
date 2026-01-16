package com.teleport.optimal_truck_load_planner.dto.requestDto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class SmartLoadRequestDto {

    @NotNull(message = "truck Details must be provided")
    @Valid
    private TruckInfo truck;

    @NotEmpty(message = "orders list must not be empty")
    @Valid
    private List<ShipmentInfo> orders;

}
