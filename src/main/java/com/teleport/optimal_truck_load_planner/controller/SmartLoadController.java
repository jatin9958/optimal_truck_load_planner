package com.teleport.optimal_truck_load_planner.controller;


import com.teleport.optimal_truck_load_planner.dto.requestDto.SmartLoadRequestDto;
import com.teleport.optimal_truck_load_planner.dto.responseDto.SmartLoadResponseDto;
import com.teleport.optimal_truck_load_planner.service.SmartLoadPlannerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class SmartLoadController {

    @Autowired
    private SmartLoadPlannerService smartLoadPlannerService;

    @PostMapping("/load-optimizer/optimize")
    public ResponseEntity<SmartLoadResponseDto> smartLoadOptimizer(@Valid @RequestBody SmartLoadRequestDto smartLoadRequestDto){
        return ResponseEntity.ok(smartLoadPlannerService.optimizeLoad(smartLoadRequestDto));
    }

}
