package com.example.health_processing_service.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Getter
@Setter
@Table(name="service_status")
public class ServiceStatusEntity {
    @Id
    private String serviceInstanceId;
    private String tenantId;
    private String serviceName;
    private String currentStatus;
    private Instant lastUpdated;
}
