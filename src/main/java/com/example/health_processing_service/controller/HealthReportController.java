package com.example.health_processing_service.controller;

import com.example.health_processing_service.domain.dto.HealthReportResponse;
import com.example.health_processing_service.service.HealthReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal/health")
@RequiredArgsConstructor
public class HealthReportController {

    private final HealthReportService reportService;

    @GetMapping("/report")
    public HealthReportResponse getOverallReport() {
        return reportService.getOverallReport();
    }

    @GetMapping("/report/{tenantId}")
    public HealthReportResponse getTenantReport(@PathVariable String tenantId) {
        return reportService.getTenantReport(tenantId);
    }
}
