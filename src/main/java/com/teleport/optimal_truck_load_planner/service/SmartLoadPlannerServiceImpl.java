package com.teleport.optimal_truck_load_planner.service;

import com.teleport.optimal_truck_load_planner.dto.requestDto.ShipmentInfo;
import com.teleport.optimal_truck_load_planner.dto.requestDto.SmartLoadRequestDto;
import com.teleport.optimal_truck_load_planner.dto.requestDto.TruckInfo;
import com.teleport.optimal_truck_load_planner.dto.responseDto.SmartLoadResponseDto;
import com.teleport.optimal_truck_load_planner.model.OptimizerResult;
import com.teleport.optimal_truck_load_planner.util.SmartLoadResponseMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SmartLoadPlannerServiceImpl implements SmartLoadPlannerService{

    private static class BestShipment {
        long payout;
        List<ShipmentInfo> shipments = new ArrayList<>();
    }

    /**
     * Optimizes truck load selection by choosing the most profitable
     * combination of shipments that satisfies weight, volume,
     * route, and safety constraints.
     *
     * @param smartLoadRequestDto request containing truck capacity
     *                            details and list of shipments
     * @return optimized load result including selected shipments,
     *         total payout, and utilization metrics
     */
    @Override
    public SmartLoadResponseDto optimizeLoad(SmartLoadRequestDto smartLoadRequestDto) {
        TruckInfo truckInfo = smartLoadRequestDto.getTruck();
        List<ShipmentInfo> validOrders = filterValidTimeWindows(smartLoadRequestDto.getOrders());
        OptimizerResult bestPayout = findBestResultAcrossRoutes(validOrders, truckInfo);
        return SmartLoadResponseMapper.toResponse(bestPayout, truckInfo);
    }

    private List<ShipmentInfo> filterValidTimeWindows(List<ShipmentInfo> orders) {
        return orders.stream()
                .filter(o -> {
                    LocalDate pickup = LocalDate.parse(o.getPickupDate());
                    LocalDate delivery = LocalDate.parse(o.getDeliveryDate());
                    return !pickup.isAfter(delivery);
                })
                .toList();
    }

    private OptimizerResult findBestResultAcrossRoutes(List<ShipmentInfo> orders, TruckInfo truckInfo) {

        if (orders == null || orders.isEmpty()) {
            return OptimizerResult.empty();
        }

        Map<String, List<ShipmentInfo>> routes =
                orders.stream()
                        .collect(Collectors.groupingBy(
                                o -> o.getOrigin() + "->" + o.getDestination()
                        ));

        OptimizerResult bestPayout = OptimizerResult.empty();

        for (List<ShipmentInfo> routeOrders : routes.values()) {

            List<ShipmentInfo> nonHazmat = routeOrders.stream()
                    .filter(o -> !o.isHazmat())
                    .collect(Collectors.toList());

            List<ShipmentInfo> hazmat = routeOrders.stream()
                    .filter(ShipmentInfo::isHazmat)
                    .collect(Collectors.toList());

            OptimizerResult nonHazmatOptimizerResult = optimize(nonHazmat, truckInfo);
            OptimizerResult hazmatOptimizerResult = optimize(hazmat, truckInfo);

            OptimizerResult bestForRoute =
                    nonHazmatOptimizerResult.getTotalPayout() >= hazmatOptimizerResult.getTotalPayout()
                            ? nonHazmatOptimizerResult
                            : hazmatOptimizerResult;

            if (bestForRoute.getTotalPayout() > bestPayout.getTotalPayout()) {
                bestPayout = bestForRoute;
            }
        }

        return bestPayout;
    }

    private OptimizerResult optimize(List<ShipmentInfo> orders, TruckInfo truckInfo) {

        if (orders.isEmpty()) {
            return OptimizerResult.empty();
        }

        orders.sort((a, b) -> Long.compare(b.getPayoutCents(), a.getPayoutCents()));

        long[] remainingMax = new long[orders.size() + 1];
        for (int i = orders.size() - 1; i >= 0; i--) {
            remainingMax[i] = remainingMax[i + 1] + orders.get(i).getPayoutCents();
        }

        BestShipment bestShipment = new BestShipment();

        backtrack(
                orders,
                0,
                0,
                0,
                0,
                new ArrayList<>(),
                remainingMax,
                truckInfo,
                bestShipment
        );

        return new OptimizerResult(bestShipment.shipments, bestShipment.payout);
    }

    private void backtrack(
            List<ShipmentInfo> orders,
            int index,
            int currentWeight,
            int currentVolume,
            long currentPayout,
            List<ShipmentInfo> currentSelection,
            long[] remainingMax,
            TruckInfo truck,
            BestShipment bestShipment
    ) {
        if (currentPayout + remainingMax[index] <= bestShipment.payout) {
            return;
        }

        // Updating best
        if (currentPayout > bestShipment.payout) {
            bestShipment.payout = currentPayout;
            bestShipment.shipments = new ArrayList<>(currentSelection);
        }

        if (index == orders.size()) {
            return;
        }

        ShipmentInfo order = orders.get(index);

        if (currentWeight + order.getWeightLbs() <= truck.getMaxWeightLbs()
                && currentVolume + order.getVolumeCuft() <= truck.getMaxVolumeCuft()) {

            currentSelection.add(order);

            backtrack(
                    orders,
                    index + 1,
                    currentWeight + order.getWeightLbs(),
                    currentVolume + order.getVolumeCuft(),
                    currentPayout + order.getPayoutCents(),
                    currentSelection,
                    remainingMax,
                    truck,
                    bestShipment
            );

            currentSelection.remove(currentSelection.size() - 1);
        }

        backtrack(
                orders,
                index + 1,
                currentWeight,
                currentVolume,
                currentPayout,
                currentSelection,
                remainingMax,
                truck,
                bestShipment
        );
    }

}
