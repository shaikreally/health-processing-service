package com.example.health_processing_service.domain.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HealthReportResponse {

    private long totalServices;
    private long upServices;
    private long downServices;
}
