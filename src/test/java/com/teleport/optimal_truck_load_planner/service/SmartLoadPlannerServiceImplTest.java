package com.teleport.optimal_truck_load_planner.service;

import com.teleport.optimal_truck_load_planner.dto.requestDto.ShipmentInfo;
import com.teleport.optimal_truck_load_planner.dto.requestDto.SmartLoadRequestDto;
import com.teleport.optimal_truck_load_planner.dto.requestDto.TruckInfo;
import com.teleport.optimal_truck_load_planner.dto.responseDto.SmartLoadResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SmartLoadPlannerServiceImplTest {

    private SmartLoadPlannerServiceImpl service;

    @BeforeEach
    void setup() {
        service = new SmartLoadPlannerServiceImpl();
    }

    @Test
    void shouldReturnEmptyResultWhenNoOrders() {

        SmartLoadRequestDto request =
                new SmartLoadRequestDto(truck(), List.of());

        SmartLoadResponseDto response = service.optimizeLoad(request);

        assertEquals(0L, response.getTotalPayoutCents());
        assertTrue(response.getSelectedOrderIds().isEmpty());
    }

    @Test
    void shouldSelectSingleOrderWhenFits() {

        ShipmentInfo order =
                shipment("ord-1", 1000, 100, 100, false);

        SmartLoadRequestDto request =
                new SmartLoadRequestDto(truck(), List.of(order));

        SmartLoadResponseDto response = service.optimizeLoad(request);

        assertEquals(1000L, response.getTotalPayoutCents());
        assertEquals(List.of("ord-1"), response.getSelectedOrderIds());
    }

    @Test
    void shouldRespectWeightConstraint() {

        ShipmentInfo heavy =
                shipment("heavy", 3000, 900, 100, false);
        ShipmentInfo light =
                shipment("light", 2000, 200, 100, false);

        SmartLoadRequestDto request =
                new SmartLoadRequestDto(truck(), List.of(heavy, light));

        SmartLoadResponseDto response = service.optimizeLoad(request);

        assertEquals(3000L, response.getTotalPayoutCents());
        assertEquals(List.of("heavy"), response.getSelectedOrderIds());
    }

    @Test
    void shouldNotMixHazmatAndNonHazmat() {

        ShipmentInfo hazmat =
                shipment("haz", 4000, 200, 200, true);
        ShipmentInfo normal =
                shipment("norm", 3500, 200, 200, false);

        SmartLoadRequestDto request =
                new SmartLoadRequestDto(truck(), List.of(hazmat, normal));

        SmartLoadResponseDto response = service.optimizeLoad(request);

        assertEquals(4000L, response.getTotalPayoutCents());
        assertEquals(List.of("haz"), response.getSelectedOrderIds());
    }

    @Test
    void shouldIgnoreInvalidTimeWindow() {

        ShipmentInfo invalid =
                ShipmentInfo.builder()
                        .id("bad")
                        .payoutCents(5000)
                        .weightLbs(100)
                        .volumeCuft(100)
                        .origin("A")
                        .destination("B")
                        .pickupDate("2025-12-10")
                        .deliveryDate("2025-12-05") // invalid
                        .isHazmat(false)
                        .build();

        SmartLoadRequestDto request =
                new SmartLoadRequestDto(truck(), List.of(invalid));

        SmartLoadResponseDto response = service.optimizeLoad(request);

        assertEquals(0L, response.getTotalPayoutCents());
        assertTrue(response.getSelectedOrderIds().isEmpty());
    }


    private TruckInfo truck() {
        return TruckInfo.builder()
                .id("truck-1")
                .maxWeightLbs(1000)
                .maxVolumeCuft(1000)
                .build();
    }

    private ShipmentInfo shipment(
            String id,
            long payout,
            int weight,
            int volume,
            boolean hazmat
    ) {
        return ShipmentInfo.builder()
                .id(id)
                .payoutCents(payout)
                .weightLbs(weight)
                .volumeCuft(volume)
                .origin("A")
                .destination("B")
                .pickupDate("2025-12-01")
                .deliveryDate("2025-12-05")
                .isHazmat(hazmat)
                .build();
    }
}