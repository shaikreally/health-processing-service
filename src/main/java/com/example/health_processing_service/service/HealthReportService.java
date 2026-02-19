package com.example.health_processing_service.service;

import com.example.health_processing_service.domain.dto.HealthReportResponse;
import com.example.health_processing_service.repository.ServiceStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HealthReportService {

    private final ServiceStatusRepository repository;

    public HealthReportResponse getOverallReport() {

        long total = repository.count();
        long up = repository.countByCurrentStatus("UP");
        long down = repository.countByCurrentStatus("DOWN");

        return HealthReportResponse.builder().totalServices(total).upServices(up).downServices(down).build();
    }

    public HealthReportResponse getTenantReport(String tenantId) {

        long up = repository.countByTenantIdAndCurrentStatus(tenantId, "UP");
        long down = repository.countByTenantIdAndCurrentStatus(tenantId, "DOWN");

        return HealthReportResponse.builder().totalServices(up + down).upServices(up).downServices(down) .build();
    }
}
