package com.datahub.controller;

import com.datahub.dto.ApiResponse;
import com.datahub.dto.DashboardDTO;
import com.datahub.service.DashboardService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping
    public ApiResponse<DashboardDTO> getDashboard() {
        return ApiResponse.success(dashboardService.getDashboardData());
    }
}
