package com.teleport.optimal_truck_load_planner.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teleport.optimal_truck_load_planner.dto.requestDto.ShipmentInfo;
import com.teleport.optimal_truck_load_planner.dto.requestDto.SmartLoadRequestDto;
import com.teleport.optimal_truck_load_planner.dto.requestDto.TruckInfo;
import com.teleport.optimal_truck_load_planner.dto.responseDto.SmartLoadResponseDto;
import com.teleport.optimal_truck_load_planner.service.SmartLoadPlannerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SmartLoadController.class)
class SmartLoadControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SmartLoadPlannerService smartLoadPlannerService;

    @Test
    void shouldReturn200WhenValidRequest() throws Exception {

        // mock service response
        when(smartLoadPlannerService.optimizeLoad(any()))
                .thenReturn(
                        SmartLoadResponseDto.builder()
                                .truckId("truck-123")
                                .totalPayoutCents(1000L)
                                .build()
                );

        TruckInfo truck = TruckInfo.builder()
                .id("truck-123")
                .maxWeightLbs(1000)
                .maxVolumeCuft(1000)
                .build();

        ShipmentInfo order = ShipmentInfo.builder()
                .id("ord-1")
                .payoutCents(1000)
                .weightLbs(100)
                .volumeCuft(100)
                .origin("A")
                .destination("B")
                .pickupDate("2025-12-01")
                .deliveryDate("2025-12-05")
                .isHazmat(false)
                .build();

        SmartLoadRequestDto request =
                new SmartLoadRequestDto(truck, List.of(order));

        mockMvc.perform(post("/api/v1/load-optimizer/optimize")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }
}
